package pl.jwizard.jwa.service.discord

import com.fasterxml.jackson.databind.JsonNode
import net.dv8tion.jda.api.JDAInfo
import org.eclipse.jetty.http.HttpMethod
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.core.server.ApiHttpHeader
import pl.jwizard.jwa.service.instance.DiscordBotApiService
import pl.jwizard.jwl.http.ApiContentType
import pl.jwizard.jwl.http.AuthTokenType
import pl.jwizard.jwl.http.SecureHttpClientService
import pl.jwizard.jwl.http.UrlSearchParamsBuilder
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.util.logger
import java.io.IOException

@Component
internal class DiscordApiService(
	private val secureHttpClientService: SecureHttpClientService,
	environment: BaseEnvironment,
) {
	companion object {
		private val log = logger<DiscordBotApiService>()
	}

	private val discordApiUrl = environment.getProperty<String>(ServerProperty.DISCORD_API_URL)
	private val appId = environment.getProperty<String>(ServerProperty.DISCORD_OAUTH_APP_ID)
	private val appSecret = environment.getProperty<String>(ServerProperty.DISCORD_OAUTH_APP_SECRET)

	fun generateOAuthToken(code: String, redirectUrl: String): OAuthTokenData {
		val searchParams = UrlSearchParamsBuilder()
			.addParam("client_id", appId)
			.addParam("client_secret", appSecret)
			.addParam("grant_type", "authorization_code")
			.addParam("code", code)
			.addParam("redirect_uri", redirectUrl)
			.buildAsFormData()
		val result = performDiscordApiRequest(
			urlSuffix = "/oauth2/token",
			httpMethod = HttpMethod.POST,
			contentType = ApiContentType.WWW_FORM_URL_ENCODED,
			body = searchParams,
		)
		return OAuthTokenData(
			accessToken = result.get("access_token").asText(),
			refreshToken = result.get("refresh_token").asText(),
			expiresIn = result.get("expires_in").asLong(),
		)
	}

	fun refreshOAuthToken(refreshToken: String): OAuthTokenData {
		val searchParams = UrlSearchParamsBuilder()
			.addParam("client_id", appId)
			.addParam("client_secret", appSecret)
			.addParam("grant_type", "refresh_token")
			.addParam("refresh_token", refreshToken)
			.buildAsFormData()
		val result = performDiscordApiRequest(
			urlSuffix = "/oauth2/token",
			httpMethod = HttpMethod.POST,
			contentType = ApiContentType.WWW_FORM_URL_ENCODED,
			body = searchParams,
		)
		return OAuthTokenData(
			accessToken = result.get("access_token").asText(),
			refreshToken = result.get("refresh_token").asText(),
			expiresIn = result.get("expires_in").asLong(),
		)
	}

	fun revokeOAuthToken(accessToken: String) {
		val searchParams = UrlSearchParamsBuilder()
			.addParam("client_id", appId)
			.addParam("client_secret", appSecret)
			.addParam("token", accessToken)
			.buildAsFormData()
		performDiscordApiRequest(
			urlSuffix = "/oauth2/token/revoke",
			httpMethod = HttpMethod.POST,
			contentType = ApiContentType.WWW_FORM_URL_ENCODED,
			body = searchParams,
		)
	}

	fun performDiscordApiRequest(
		urlSuffix: String,
		authToken: String? = null,
		authTokenType: AuthTokenType = AuthTokenType.BEARER,
		httpMethod: HttpMethod = HttpMethod.GET,
		contentType: ApiContentType = ApiContentType.APPLICATION_JSON,
		body: String? = null,
	): JsonNode = try {
		val response = secureHttpClientService.prepareAndRunSecureHttpRequest(
			url = "$discordApiUrl$urlSuffix",
			authToken = authToken,
			authTokenType = authTokenType,
			contentType = contentType,
			httpMethod = httpMethod,
			headers = mapOf(
				// https://discord.com/developers/docs/reference#user-agent
				ApiHttpHeader.USER_AGENT to "DiscordBot (JDA, ${JDAInfo.VERSION})"
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
