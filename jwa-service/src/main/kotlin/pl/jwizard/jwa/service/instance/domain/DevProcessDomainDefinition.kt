package pl.jwizard.jwa.service.instance.domain

import pl.jwizard.jwa.service.instance.InstanceProperty
import pl.jwizard.jwl.vault.kvgroup.VaultKvGroupProperties

internal class DevProcessDomainDefinition : ProcessDomainDefinition {
	override val runtimeProfiles = listOf("dev")

	override fun generatePaths(
		pathDefinition: String,
		properties: VaultKvGroupProperties<InstanceProperty>,
	): List<ProcessDefinition> {
		// for development purposes, we assume that app has 2 instances, with one cluster per instance
		// cluster has one running-instance defined by port (in localhost environment)
		val shardsPerProcess = properties.get<Int>(InstanceProperty.SHARDS_PER_PROCESS)
		val serverPort = properties.get<Int>(InstanceProperty.SERVER_PORT)

		val processDefinition = ProcessDefinition(
			domain = pathDefinition.format(serverPort),
			countOfShards = shardsPerProcess,
		)
		return listOf(processDefinition)
	}
}
