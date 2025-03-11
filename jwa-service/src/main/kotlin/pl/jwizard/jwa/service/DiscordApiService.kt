package pl.jwizard.jwa.service

import com.fasterxml.jackson.databind.JsonNode
import net.dv8tion.jda.api.JDAInfo
import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.http.HttpMethod
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.service.http.ApiContentType
import pl.jwizard.jwa.service.http.AuthTokenType
import pl.jwizard.jwa.service.http.SecureHttpService
import pl.jwizard.jwa.service.instance.DiscordBotApiService
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.util.logger
import java.io.IOException

@Component
internal class DiscordApiService(
	private val secureHttpService: SecureHttpService,
	environment: BaseEnvironment,
) {
	companion object {
		private val log = logger<DiscordBotApiService>()
	}

	private val discordApiUrl = environment.getProperty<String>(ServerProperty.DISCORD_API_URL)

	fun performDiscordApiRequest(
		urlSuffix: String,
		authToken: String? = null,
		authTokenType: AuthTokenType = AuthTokenType.BEARER,
		httpMethod: HttpMethod = HttpMethod.GET,
		contentType: ApiContentType = ApiContentType.APPLICATION_JSON,
		body: String? = null,
	): JsonNode = try {
		val response = secureHttpService.prepareAndRunSecureHttpRequest(
			url = "$discordApiUrl$urlSuffix",
			authToken = authToken,
			authTokenType = authTokenType,
			contentType = contentType,
			httpMethod = httpMethod,
			headers = mapOf(
				// https://discord.com/developers/docs/reference#user-agent
				HttpHeader.USER_AGENT to "DiscordBot (JDA, ${JDAInfo.VERSION})"
			),
			body = body,
		)
		if (response == null) {
			throw IOException("response object is null")
		}
		response
	} catch (ex: IOException) {
		log.error(
			"Unable to perform Discord API http request for url: {}. Cause: {}.",
			urlSuffix,
			ex.message,
		)
		throw ex
	}
}
