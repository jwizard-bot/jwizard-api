package pl.jwizard.jwa.service.instance

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.service.http.SecureHttpService
import pl.jwizard.jwa.service.instance.domain.DevProcessDomainDefinition
import pl.jwizard.jwa.service.instance.domain.ProcessDefinition
import pl.jwizard.jwa.service.instance.domain.ProdProcessDomainDefinition
import pl.jwizard.jwl.IrreparableException
import pl.jwizard.jwl.property.AppBaseListProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.vault.VaultClient
import pl.jwizard.jwl.vault.kvgroup.VaultKvGroupProperties

@Component
internal class BotInstancesService(
	private val secureHttpService: SecureHttpService,
	environment: BaseEnvironment,
) {
	private val vaultClient = VaultClient(environment)

	private val runtimeProfiles = environment
		.getListProperty<String>(AppBaseListProperty.RUNTIME_PROFILES)
	private val processUrlFragment = environment
		.getProperty<String>(ServerProperty.DISCORD_PROCESS_URL_FRAGMENT)

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

	// key is instance id, value is map of domain with count of shards per domain (process)
	val instanceDomains: Map<Int, List<ProcessDefinition>>
		get() {
			val definitions = listOf(DevProcessDomainDefinition(), ProdProcessDomainDefinition())
			val selectedClusterDefinition = definitions
				.find { it.runtimeProfiles.containsAll(runtimeProfiles) }
				?: throw IrreparableException(
					this::class,
					"Unable to find process definition for selected runtime profiles.",
				)
			return instanceProperties.entries.associate {
				val processDefinition = selectedClusterDefinition
					.generatePaths(processUrlFragment, it.value)
				it.key to processDefinition
			}
		}

	fun getSafetyProperties(instanceKey: Int) = instanceProperties[instanceKey]!!

	fun createInstanceName(instanceKey: Int): String {
		val instanceIndex = if (instanceKey > 0) " ($instanceKey)" else ""
		return "JWizard$instanceIndex"
	}

	fun getInstanceColor(instanceKey: Int): String {
		val properties = getSafetyProperties(instanceKey)
		return properties.get(InstanceProperty.JDA_PRIMARY_COLOR)
	}

	fun performHttpRequest(domain: String, urlSuffix: String, instanceId: Int): JsonNode? {
		val properties = getSafetyProperties(instanceId)
		return secureHttpService.prepareAndRunSecureHttpRequest(
			url = "$domain/api/v1/status$urlSuffix",
			authToken = properties.get<String>(InstanceProperty.REST_API_TOKEN),
			silent = true,
			withProxyVerifyToken = true,
		)
	}
}
