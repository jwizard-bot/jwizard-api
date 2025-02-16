package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.service.dto.BotInstanceData
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.vault.VaultClient
import pl.jwizard.jwl.util.getUserIdFromToken

@SingletonService
class BotInstancesServiceBean(environment: EnvironmentBean) {
	private val vaultClient = VaultClient(environment)
	private val instancePattern = Regex("^core-instance-\\d+$")

	final val botInstances = mutableMapOf<Int, BotInstanceData>()

	init {
		vaultClient.init()
		botInstances += getBotInstancesData()
	}

	private fun getBotInstancesData(): Map<Int, BotInstanceData> {
		val botInstances = vaultClient.readKvPaths(patternFilter = instancePattern)
		val fetchedInstances = mutableMapOf<Int, BotInstanceData>()
		for (botInstance in botInstances) {
			val instanceId = botInstance.substringAfterLast("-").toInt()
			val instanceProperties = vaultClient.readKvSecrets(botInstance)
			val appId = getUserIdFromToken(instanceProperties.getProperty("V_JDA_SECRET")) ?: continue
			fetchedInstances[instanceId] = BotInstanceData(
				appId,
				color = "#%06X".format(Integer.decode(instanceProperties.getProperty("V_JDA_PRIMARY_COLOR"))),
				instancePrefix = instanceProperties.getProperty("V_JDA_INSTANCE_PREFIX"),
				shardsPerCluster = instanceProperties.getProperty("V_SHARDS_PER_CLUSTER").toInt(),
				shardOverallMax = instanceProperties.getProperty("V_SHARD_OVERALL_MAX").toInt(),
			)
		}
		return fetchedInstances
	}
}
