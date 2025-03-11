package pl.jwizard.jwa.service.instance

import com.fasterxml.jackson.databind.JsonNode
import org.eclipse.jetty.http.HttpMethod
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.util.UrlSearchParamsBuilder
import pl.jwizard.jwa.service.DiscordApiService
import pl.jwizard.jwa.service.http.ApiContentType
import pl.jwizard.jwa.service.http.AuthTokenType
import pl.jwizard.jwl.util.getUserIdFromToken

@Component
internal class DiscordBotApiService(
	private val discordApiService: DiscordApiService,
	private val botInstancesService: BotInstancesService,
) {
	fun getApplicationAvatarUrl(instanceId: Int, size: Int?): String {
		val properties = botInstancesService.getSafetyProperties(instanceId)
		val resNode = performDiscordApiRequestAsBot(urlSuffix = "/applications/@me", instanceId)

		val iconNode = resNode.get("icon")
		val appId = getUserIdFromToken(properties.get<String>(InstanceProperty.JDA_SECRET))

		return if (!iconNode.isNull) {
			UrlSearchParamsBuilder()
				.setBaseUrl("https://cdn.discordapp.com/avatars/${appId}/${iconNode.asText()}.png")
				.apply { size?.let { addParam("size", it) } }
				.build()
		} else {
			"https://discord.com/assets/$appId.png"
		}
	}

	private fun performDiscordApiRequestAsBot(
		urlSuffix: String,
		instanceId: Int,
		httpMethod: HttpMethod = HttpMethod.GET,
		contentType: ApiContentType = ApiContentType.APPLICATION_JSON,
		body: String? = null,
	): JsonNode {
		val properties = botInstancesService.getSafetyProperties(instanceId)
		return discordApiService.performDiscordApiRequest(
			urlSuffix = urlSuffix,
			httpMethod = httpMethod,
			authToken = properties.get<String>(InstanceProperty.JDA_SECRET),
			authTokenType = AuthTokenType.BOT,
			contentType = contentType,
			body = body,
		)
	}
}
