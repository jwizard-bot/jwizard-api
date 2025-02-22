package pl.jwizard.jwa.service.instance.domain

import pl.jwizard.jwa.service.instance.InstanceProperty
import pl.jwizard.jwl.vault.kvgroup.VaultKvGroupProperties
import kotlin.math.ceil
import kotlin.math.min

internal class ProdProcessDomainDefinition : ProcessDomainDefinition {
	override val runtimeProfiles = listOf("prod")

	override fun generatePaths(
		pathDefinition: String,
		properties: VaultKvGroupProperties<InstanceProperty>,
	): List<ProcessDefinition> {
		val instancePrefix = properties.get<String>(InstanceProperty.JDA_INSTANCE_PREFIX)

		// for production purposes, we return url addresses based on shards per process and
		// maximum shards (for instance)
		val shardsPerProcess = properties.get<Int>(InstanceProperty.SHARDS_PER_PROCESS)
		val shardOverallMax = properties.get<Int>(InstanceProperty.SHARD_OVERALL_MAX)

		val totalProcesses = ceil(shardOverallMax / shardsPerProcess.toDouble()).toInt()

		val pathDefinitions = mutableListOf<ProcessDefinition>()

		var shardNumber = 0
		for (clusterId in 0 until totalProcesses) {
			val endShardNumber = min(shardOverallMax, shardNumber + shardsPerProcess - 1)
			pathDefinitions += ProcessDefinition(
				domain = pathDefinition.format(
					instancePrefix,
					shardNumber, // start shard id
					endShardNumber // end shard id
				),
				// pre-defined total shards per instance
				countOfShards = endShardNumber - shardNumber,
			)
			shardNumber += shardsPerProcess
		}
		return pathDefinitions.toList()
	}
}
