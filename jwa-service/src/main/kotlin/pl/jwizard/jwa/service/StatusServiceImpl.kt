package pl.jwizard.jwa.service

import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.util.BlockingThreadsExecutor
import pl.jwizard.jwa.core.util.convertMillisToDtf
import pl.jwizard.jwa.gateway.http.rest.route.status.StatusService
import pl.jwizard.jwa.gateway.http.rest.route.status.dto.*
import pl.jwizard.jwa.service.audionode.AudioNodeProperty
import pl.jwizard.jwa.service.audionode.AudioNodeService
import pl.jwizard.jwa.service.instance.BotInstancesService
import pl.jwizard.jwa.service.instance.DiscordBotApiService
import pl.jwizard.jwa.service.instance.domain.ProcessDefinition
import pl.jwizard.jwl.util.ext.getAsInt
import pl.jwizard.jwl.util.ext.getAsLong
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.ceil

@Component
internal class StatusServiceImpl(
	private val botInstancesService: BotInstancesService,
	private val discordBotApiService: DiscordBotApiService,
	private val audioNodeService: AudioNodeService,
) : StatusService {
	companion object {
		private const val STATUS_PAGE_URL = "https://status.jwizard.pl"
	}

	override fun getGlobalStatus(): GlobalStatusResDto {
		// get other instances state
		val document = Jsoup.connect("$STATUS_PAGE_URL/badge").get()
		val svgIcon = document.selectFirst("svg#root")
		var operational: Boolean? = null
		if (svgIcon != null) {
			val classNames = svgIcon.classNames()
			operational = when {
				classNames.contains("text-statuspage-green") -> true
				classNames.contains("text-statuspage-red") -> false
				else -> null
			}
		}
		// get bot instances (processes and shards) state
		var shardsDown = 0
		for ((instanceKey, domains) in botInstancesService.instanceDomains.entries) {
			for (domainDefinition in domains) {
				val shardsStatusCount = botInstancesService
					.performHttpRequest(domainDefinition.domain, "/shard/count", instanceKey)
				if (shardsStatusCount == null) {
					shardsDown += domainDefinition.countOfShards
					continue
				}
				val shardsUp = shardsStatusCount.getAsInt("up")
				shardsDown += (domainDefinition.countOfShards - shardsUp)
			}
		}
		val globalStatus = if (operational == null) {
			// some other services state are unknown
			null
		} else {
			if (shardsDown == 0 && operational == true) {
				// all shards and other instances are up
				true
			} else if (shardsDown > 0 || operational == false) {
				// some shards are down or other instances are down
				false
			} else {
				// otherwise return false (we assume, that is an issue)
				false
			}
		}
		return GlobalStatusResDto(
			globalUp = globalStatus,
			externalServicesWebsiteUrl = STATUS_PAGE_URL,
		)
	}

	override fun getInstanceShardsStatus(instanceId: Int): List<ShardStatusResDto> {
		val combinedShardsStatus = mutableListOf<ShardStatusResDto>()
		val domains = botInstancesService.instanceDomains.getOrDefault(instanceId, emptyList())
		for ((index, processDefinition) in domains.withIndex()) {
			val shardsInfoNode = botInstancesService
				.performHttpRequest(processDefinition.domain, "/shard/all", instanceId)

			val allShardsAsUnavailable = (0 until processDefinition.countOfShards)
				.map { ShardStatusResDto(false, it, index) }

			// if process is available
			if (shardsInfoNode != null) {
				val availableShardIds = shardsInfoNode.map { it.getAsInt("id") }

				// add missing shards (get all shards as unavailable and exclude already fetched)
				combinedShardsStatus += allShardsAsUnavailable
					.filter { !availableShardIds.contains(it.globalShardId) }

				combinedShardsStatus += shardsInfoNode.map {
					ShardStatusResDto(
						up = true,
						globalShardId = it.getAsInt("id"),
						// cluster group is separated process on different domains
						processGroupId = index,
						gatewayPing = it.getAsLong("ping"),
						servers = it.getAsInt("servers"),
						users = it.getAsInt("users"),
						activeAudioPlayers = it.getAsInt("activeAudioPlayers"),
						audioListeners = it.getAsInt("audioListeners"),
					)
				}
			} else {
				combinedShardsStatus += allShardsAsUnavailable
			}
		}
		return combinedShardsStatus.sortedBy { it.globalShardId }.toList()
	}

	override fun getAudioNodesStatus(): AudioNodesStatusResDto {
		val onlyActiveNodes = audioNodeService.audioNodeProperties.entries
			.filter { it.value.get<Boolean>(AudioNodeProperty.ACTIVE) }

		val audioNodesStatus = onlyActiveNodes.withIndex().map { (index, entry) ->
			val (key, properties) = entry

			val (infoTime, infoRes) = audioNodeService.performAudioNodeApiRequestWithTime("/info", key)
			val (statsTime, statsRes) = audioNodeService.performAudioNodeApiRequestWithTime("/stats", key)

			val baseObject = AudioNodeStatus(
				up = infoRes != null && statsRes != null,
				id = index,
				name = properties.get(AudioNodeProperty.NAME),
				pool = properties.get(AudioNodeProperty.NODE_POOL),
				region = properties.get(AudioNodeProperty.REGION_GROUP),
			)
			if (infoRes == null || statsRes == null) {
				baseObject
			} else {
				baseObject.copy(
					version = infoRes.get("version").get("semver").asText(),
					lavaplayerVersion = infoRes.get("lavaplayer").asText(),
					players = statsRes.get("players").asInt(),
					playingPlayers = statsRes.get("playingPlayers").asInt(),
					uptime = convertMillisToDtf(statsRes.get("uptime").asLong()),
					responseTime = ceil(((infoTime.toDouble() + statsTime) / 2)).toLong()
				)
			}
		}
		return AudioNodesStatusResDto(STATUS_PAGE_URL, audioNodesStatus)
	}

	override fun getInstancesStatus(avatarSize: Int?): List<InstanceStatusResDto> {
		// get N of threads based on count of instances
		val instances = botInstancesService.instanceDomains.entries.toList()
		val blockingThreadsExecutor = BlockingThreadsExecutor(
			poolSize = instances.size,
			futureCallback = { instance: Map.Entry<Int, List<ProcessDefinition>> ->
				val (instanceKey, domains) = instance
				val statusList = domains.mapNotNull {
					botInstancesService.performHttpRequest(it.domain, "/shard/reduced", instanceKey)
				}
				val totalRunningShards = statusList.sumOf { it.getAsInt("totalShards") }
				val avgShardGatewayPing = statusList.map { it.getAsLong("avgShardGatewayPing") }.average()
				val reducedServers = statusList.sumOf { it.getAsInt("servers") }
				val reducedUsers = statusList.sumOf { it.getAsInt("users") }
				val reducedActiveAudioPlayers = statusList.sumOf { it.getAsInt("activeAudioPlayers") }
				val reducedAudioListeners = statusList.sumOf { it.getAsInt("audioListeners") }
				InstanceStatusResDto(
					id = instanceKey,
					name = botInstancesService.createInstanceName(instanceKey),
					color = botInstancesService.getInstanceColor(instanceKey),
					avatarUrl = discordBotApiService.getApplicationAvatarUrl(instanceKey, avatarSize),
					shards = RunningStatusCount(
						up = totalRunningShards,
						down = domains.sumOf { it.countOfShards } - totalRunningShards
					),
					processes = RunningStatusCount(
						up = statusList.size,
						down = domains.size - statusList.size
					),
					avgShardGatewayPing = ceil(avgShardGatewayPing).toLong(),
					avgShardsPerProcess = ceil(totalRunningShards.toDouble() / domains.size).toInt(),
					servers = reducedServers,
					users = reducedUsers,
					activeAudioPlayers = reducedActiveAudioPlayers,
					audioListeners = reducedAudioListeners,
				)
			}
		)
		blockingThreadsExecutor.initThreads(instances)
		return blockingThreadsExecutor.waitAndGet()
	}

	override fun checkIfShardForGuildNameOrIdIsUp(
		guildNameOrId: String,
		instanceId: Int?,
	): ShardCheckResDto {
		// return all instances when instanceId is null or return specific instance
		val checkedInstances = botInstancesService.instanceDomains
			.filterKeys { instanceId == null || instanceId == -1 || it == instanceId }

		// calculate count of threads == all processes from all instances
		val countOfThreads = checkedInstances.values.flatten().sumOf { it.countOfShards }
		val executor = Executors.newFixedThreadPool(countOfThreads)

		// when this variable is set to true, stop all threads
		val stopExecution = AtomicBoolean(false)

		// we must flat <key, list<domain>> structure to list<pair<key, domain>> (map cannot have
		// same keys, also thread executor requires single level data structure
		val flattedInstances = checkedInstances
			.flatMap { it.value.map { value -> it.key to value } }

		val futures = flattedInstances.map { (instanceKey, domainDefinition) ->
			executor.submit(Callable {
				if (stopExecution.get()) {
					return@Callable false
				}
				val response = botInstancesService.performHttpRequest(
					domainDefinition.domain,
					urlSuffix = "/shard/check?guild=$guildNameOrId",
					instanceKey,
				)
				val result = response?.get("status")?.asBoolean() ?: false
				if (result) {
					// stop all threads, when found value
					stopExecution.set(true)
				}
				result
			})
		}
		val status = futures.any { future ->
			try {
				val result = future.get()
				if (result) {
					// cancel rest of the threads, if any return true
					futures.forEach { it.cancel(true) }
				}
				result
			} catch (e: Exception) {
				false
			}
		}
		executor.shutdown()
		return ShardCheckResDto(status)
	}
}
