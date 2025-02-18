package pl.jwizard.jwa.service.instance

import org.springframework.stereotype.Component
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.vault.VaultClient
import pl.jwizard.jwl.vault.kvgroup.VaultKvGroupProperties

@Component
internal class BotInstancesService(environment: BaseEnvironment) {
	private val vaultClient = VaultClient(environment)

	final var instanceProperties: Map<Int, VaultKvGroupProperties<InstanceProperty>>
		private set

	init {
		vaultClient.initOnce()
		// bot instances properties are fetch ONLY at start application
		// possibly in future add some webhook, which may refresh instance properties without
		// restarting whole app
		instanceProperties = vaultClient.readKvGroupPropertySource(
			kvPath = "core-instance",
			patternFilter = Regex("^\\d+$"),
			keyExtractor = { it.toInt() }
		)
	}

	fun getSafetyProperties(instanceKey: Int) = instanceProperties[instanceKey]!!

	fun createInstanceName(instanceKey: Int): String {
		val instanceIndex = if (instanceKey > 0) " ($instanceKey)" else ""
		return "JWizard$instanceIndex"
	}
}
