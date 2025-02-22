package pl.jwizard.jwa.service.instance.domain

import pl.jwizard.jwa.service.instance.InstanceProperty
import pl.jwizard.jwl.vault.kvgroup.VaultKvGroupProperties

internal interface ProcessDomainDefinition {
	val runtimeProfiles: List<String>

	// return map where key is domain and int is shards count per this domain (process)
	fun generatePaths(
		pathDefinition: String,
		properties: VaultKvGroupProperties<InstanceProperty>,
	): List<ProcessDefinition>
}
