package pl.jwizard.jwa.service

import net.dv8tion.jda.api.Permission
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.util.UrlSearchParamsBuilder
import pl.jwizard.jwa.http.route.invite.InviteService
import pl.jwizard.jwa.service.instance.BotInstancesService
import pl.jwizard.jwa.service.instance.InstanceProperty
import pl.jwizard.jwl.property.AppBaseListProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.util.getUserIdFromToken

@Component
internal class InviteServiceImpl(
	private val botInstancesService: BotInstancesService,
	environment: BaseEnvironment,
) : InviteService {
	private val permissions = environment.getListProperty<String>(AppBaseListProperty.JDA_PERMISSIONS)

	override fun createInviteRedirectUrl(instanceId: Int): String? {
		val properties = botInstancesService.instanceProperties[instanceId] ?: return null
		val appId = getUserIdFromToken(properties.get(InstanceProperty.JDA_SECRET))

		val mappedPermissions = permissions.map { Permission.valueOf(it) }
		val rawPermissions = Permission.getRaw(mappedPermissions)

		return UrlSearchParamsBuilder()
			.setBaseUrl("https://discord.com/oauth2/authorize")
			.apply { appId?.let { id -> addParam("client_id", id) } }
			.addParam("scope", "bot")
			.addParam("permissions", rawPermissions)
			.build()
	}
}
