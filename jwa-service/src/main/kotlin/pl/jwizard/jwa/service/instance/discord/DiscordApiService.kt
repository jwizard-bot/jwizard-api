package pl.jwizard.jwa.service.instance.discord

import com.fasterxml.jackson.databind.JsonNode
import io.javalin.http.ContentType
import io.javalin.http.InternalServerErrorResponse
import net.dv8tion.jda.api.JDAInfo
import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.http.HttpMethod
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.core.util.UrlSearchParamsBuilder
import pl.jwizard.jwa.service.SecureHttpService
import pl.jwizard.jwa.service.instance.BotInstancesService
import pl.jwizard.jwa.service.instance.InstanceProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.util.getUserIdFromToken
import pl.jwizard.jwl.util.logger
import java.io.IOException

@Component
internal class DiscordApiService(
	private val secureHttpService: SecureHttpService,
	private val botInstancesService: BotInstancesService,
	environment: BaseEnvironment,
) {
	companion object {
		private val log = logger<DiscordApiService>()
	}

	private val discordApiUrl = environment.getProperty<String>(ServerProperty.DISCORD_API_URL)

	fun getApplicationAvatarUrl(instanceId: Int, size: Int?): String {
		val properties = botInstancesService.getSafetyProperties(instanceId)
		val resNode = performDiscordApiRequest(urlSuffix = "/applications/@me", instanceId)

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

	private fun performDiscordApiRequest(
		urlSuffix: String,
		instanceId: Int,
		httpMethod: HttpMethod = HttpMethod.GET,
		contentType: ContentType = ContentType.APPLICATION_JSON,
		body: String? = null
	): JsonNode {
		val properties = botInstancesService.getSafetyProperties(instanceId)
		val jdaSecret = properties.get<String>(InstanceProperty.JDA_SECRET)
		var errorMessage: String? = null
		val response = try {
			secureHttpService.prepareAndRunSecureHttpRequest(
				url = "$discordApiUrl$urlSuffix",
				token = "Bot $jdaSecret",
				contentType = contentType,
				httpMethod = httpMethod,
				headers = mapOf(
					// https://discord.com/developers/docs/reference#user-agent
					HttpHeader.USER_AGENT to "DiscordBot (JDA, ${JDAInfo.VERSION})"
				),
				body = body,
			)
		} catch (ex: IOException) {
			errorMessage = ex.message
			null
		}
		if (response == null) {
			log.error(
				"Unable to perform Discord API http request for url: {}. Cause: {}.",
				urlSuffix,
				errorMessage ?: "response object is null",
			)
			throw InternalServerErrorResponse()
		}
		return response
	}
}
