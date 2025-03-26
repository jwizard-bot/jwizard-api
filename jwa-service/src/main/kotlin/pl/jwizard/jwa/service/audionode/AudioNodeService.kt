package pl.jwizard.jwa.service.audionode

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.service.http.SecureHttpService
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.vault.VaultClient
import pl.jwizard.jwl.vault.kvgroup.VaultKvGroupProperties

@Component
internal class AudioNodeService(
	private val secureHttpService: SecureHttpService,
	environment: BaseEnvironment,
) {
	private val vaultClient = VaultClient(environment)
	private val apiVersion = environment.getProperty<String>(ServerProperty.LAVALINK_API_VERSION)

	final var audioNodeProperties: Map<Int, VaultKvGroupProperties<AudioNodeProperty>>
		private set

	init {
		vaultClient.initOnce()
		audioNodeProperties = vaultClient.readKvGroupPropertySource(
			kvPath = "audio-node",
			patternFilter = Regex("^\\d+$"),
			keyExtractor = { it.toInt() }
		)
	}

	fun performAudioNodeApiRequestWithTime(
		urlSuffix: String,
		audioNodeId: Int,
	): Pair<Long, JsonNode?> {
		val startTime = System.nanoTime()
		val response = performAudioNodeApiRequest(urlSuffix, audioNodeId)
		val elapsedTime = (System.nanoTime() - startTime) / 1_000_000
		return Pair(elapsedTime, response)
	}

	fun performAudioNodeApiRequest(urlSuffix: String, audioNodeId: Int): JsonNode? {
		val properties = getSafetyProperties(audioNodeId)
		var scheme = "http"
		if (properties.get(AudioNodeProperty.SECURE)) {
			scheme += "s"
		}
		val gatewayHost = properties.get<String>(AudioNodeProperty.GATEWAY_HOST)
		return secureHttpService.prepareAndRunSecureHttpRequest(
			url = "$scheme://$gatewayHost/$apiVersion$urlSuffix",
			authToken = properties.get(AudioNodeProperty.PASSWORD),
			silent = true,
			withProxyVerification = true,
		)
	}

	private fun getSafetyProperties(instanceKey: Int) = audioNodeProperties[instanceKey]!!
}
