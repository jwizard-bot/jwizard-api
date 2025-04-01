package pl.jwizard.jwa.service

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.gateway.http.rest.route.join.JoinService
import pl.jwizard.jwa.gateway.http.rest.route.join.dto.JoinInstanceResDto
import pl.jwizard.jwa.service.instance.BotInstancesService
import pl.jwizard.jwa.service.instance.DiscordBotApiService
import pl.jwizard.jwl.property.AppBaseListProperty
import pl.jwizard.jwl.property.BaseEnvironment

@Component
internal class JoinServiceImpl(
	private val botInstancesService: BotInstancesService,
	private val discordBotApiService: DiscordBotApiService,
	environment: BaseEnvironment,
) : JoinService {
	private val selfUrl = environment.getProperty<String>(ServerProperty.SERVER_SELF_URL)
	private val permissions = environment.getListProperty<String>(AppBaseListProperty.JDA_PERMISSIONS)

	override fun fetchJoinInstances(
		avatarSize: Int?,
	) = botInstancesService.instanceProperties.keys.map {
		JoinInstanceResDto(
			name = botInstancesService.createInstanceName(it),
			color = botInstancesService.getInstanceColor(it),
			link = "$selfUrl/invite/bot/${it}",
			avatarUrl = discordBotApiService.getApplicationAvatarUrl(it, avatarSize),
		)
	}

	override fun fetchRequiredPermissions() = permissions.sorted()
}
