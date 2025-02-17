package pl.jwizard.jwa.service.instance

import org.springframework.stereotype.Component
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.vault.VaultClient

@Component
internal class BotInstancesService(environment: BaseEnvironment) {
	private val vaultClient = VaultClient(environment)
	private val instancePattern = Regex("^core-instance-\\d+$")

	final val instanceProperties = mutableMapOf<Int, InstanceProperties>()

	init {
		vaultClient.init()
		// bot instance properties are fetch ONLY at start application
		// possibly in future add some webhook, which may refresh instance properties without
		// restarting whole app
		instanceProperties += fetchInstanceProperties()
	}

	fun getSafetyProperties(instanceKey: Int) = instanceProperties[instanceKey]!!

	fun createInstanceName(instanceKey: Int): String {
		val instanceIndex = if (instanceKey > 0) " ($instanceKey)" else ""
		return "JWizard$instanceIndex"
	}

	private fun fetchInstanceProperties(): Map<Int, InstanceProperties> {
		val botInstances = vaultClient.readKvPaths(patternFilter = instancePattern)
		return botInstances.associate {
			val instanceId = it.substringAfterLast("-").toInt()
			instanceId to InstanceProperties(vaultClient.readKvSecrets(it))
		}
	}
}
