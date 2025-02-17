package pl.jwizard.jwa.service

import net.dv8tion.jda.api.Permission
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.rest.route.join.dto.JoinInstanceResDto
import pl.jwizard.jwa.rest.route.join.spi.JoinService
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseListProperty
import pl.jwizard.jwl.util.UrlSearchParams

@SingletonService
internal class JoinServiceBean(
	environment: EnvironmentBean,
	private val botInstancesService: BotInstancesServiceBean
) : JoinService {
	private val permissions = environment.getListProperty<String>(AppBaseListProperty.JDA_PERMISSIONS)

	override fun fetchJoinInstances(): List<JoinInstanceResDto> {
		val mappedPermissions = permissions.map { Permission.valueOf(it) }
		val rawPermissions = Permission.getRaw(mappedPermissions)

		return botInstancesService.botInstances.entries.map { (id, data) ->
			val instanceIndex = if (id > 0) " ($id)" else ""

			val joinLink = UrlSearchParams.Builder()
				.setBaseUrl("https://discord.com/oauth2/authorize")
				.addParam("client_id", data.appId)
				.addParam("scope", "bot")
				.addParam("permissions", rawPermissions)
				.build()

			JoinInstanceResDto(
				name = "JWizard$instanceIndex",
				color = data.color,
				link = joinLink
			)
		}
	}

	override fun fetchRequiredPermissions() = permissions.sorted()
}
