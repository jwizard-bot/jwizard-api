package pl.jwizard.jwa.service.instance

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.eclipse.jetty.http.HttpHeader
import org.springframework.stereotype.Component
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.vault.VaultClient
import pl.jwizard.jwl.vault.kvgroup.VaultKvGroupProperties
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
internal class BotInstancesService(
	private val httpClient: HttpClient,
	private val objectMapper: ObjectMapper,
	environment: BaseEnvironment,
) {
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

	fun performHttpRequest(domain: String, urlSuffix: String, instanceId: Int): JsonNode? {
		val properties = getSafetyProperties(instanceId)
		val token = properties.get<String>(InstanceProperty.REST_API_TOKEN)

		val httpRequest = HttpRequest.newBuilder()
			.uri(URI.create("$domain/api/v1/status$urlSuffix"))
			.header(HttpHeader.AUTHORIZATION.asString(), token)
			.build()

		val response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
		return if (response.statusCode() == 200) {
			objectMapper.readTree(response.body())
		} else {
			null
		}
	}
}
