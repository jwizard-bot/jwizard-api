package pl.jwizard.jwa.service.auth

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.core.util.UrlSearchParamsBuilder
import pl.jwizard.jwa.service.http.SecureHttpService
import pl.jwizard.jwl.property.BaseEnvironment

@Component
internal class GeolocationService(
	private val secureHttpService: SecureHttpService,
	environment: BaseEnvironment,
) {
	private val apiUrl = environment.getProperty<String>(ServerProperty.GEOLOCATION_API_URL)
	private val apiKey = environment.getProperty<String>(ServerProperty.GEOLOCATION_API_KEY)

	fun extractGeolocationBasedIp(ip: String): String? {
		if (apiKey.isBlank()) {
			// geolocation service is disabled
			return null
		}
		val fields = listOf("country_name", "state_prov", "city")
		val searchUrl = UrlSearchParamsBuilder()
			.setBaseUrl(apiUrl)
			.addParam("apiKey", apiKey)
			.addParam("ip", ip)
			.addParam("fields", fields.joinToString())
			.build()

		val response = secureHttpService
			.prepareAndRunSecureHttpRequest(searchUrl, silent = true)
			?: return null

		return fields.joinToString { response.get(it).asText() }
	}
}
