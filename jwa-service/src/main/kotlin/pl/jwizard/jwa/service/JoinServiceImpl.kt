package pl.jwizard.jwa.service

import net.dv8tion.jda.api.Permission
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.util.UrlSearchParamsBuilder
import pl.jwizard.jwa.rest.route.join.JoinService
import pl.jwizard.jwa.rest.route.join.dto.JoinInstanceResDto
import pl.jwizard.jwa.service.instance.BotInstancesService
import pl.jwizard.jwa.service.instance.InstanceProperty
import pl.jwizard.jwl.property.AppBaseListProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.util.getUserIdFromToken

@Component
internal class JoinServiceImpl(
	private val botInstancesService: BotInstancesService,
	environment: BaseEnvironment,
) : JoinService {
	private val permissions = environment.getListProperty<String>(AppBaseListProperty.JDA_PERMISSIONS)

	override fun fetchJoinInstances(): List<JoinInstanceResDto> {
		val mappedPermissions = permissions.map { Permission.valueOf(it) }
		val rawPermissions = Permission.getRaw(mappedPermissions)

		return botInstancesService.instanceProperties.entries.map { (id, properties) ->
			val appId = getUserIdFromToken(properties.get(InstanceProperty.JDA_SECRET))

			val joinLink = UrlSearchParamsBuilder()
				.setBaseUrl("https://discord.com/oauth2/authorize")
				.apply { appId?.let { id -> addParam("client_id", id) } }
				.addParam("scope", "bot")
				.addParam("permissions", rawPermissions)
				.build()

			JoinInstanceResDto(
				name = botInstancesService.createInstanceName(id),
				color = properties.get(InstanceProperty.JDA_PRIMARY_COLOR),
				link = joinLink
			)
		}
	}

	override fun fetchRequiredPermissions() = permissions.sorted()
}
