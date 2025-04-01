package pl.jwizard.jwa.service

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.i18n.I18nUtilLocaleSource
import pl.jwizard.jwa.gateway.http.dto.OptionsResDto
import pl.jwizard.jwa.gateway.http.rest.route.instance.InstanceService
import pl.jwizard.jwa.gateway.http.rest.route.instance.dto.InstanceDefinitionResDto
import pl.jwizard.jwa.gateway.http.rest.route.instance.dto.InstanceOption
import pl.jwizard.jwa.service.instance.BotInstancesService
import pl.jwizard.jwa.service.instance.DiscordBotApiService
import pl.jwizard.jwl.i18n.I18n

@Component
internal class InstanceServiceImpl(
	private val i18n: I18n,
	private val botInstancesService: BotInstancesService,
	private val discordBotApiService: DiscordBotApiService,
) : InstanceService {

	override fun getAllInstanceOptions(language: String?): OptionsResDto<InstanceOption> {
		val allOptions = mutableListOf<InstanceOption>()
		val defaultOption = InstanceOption(-1, i18n.t(I18nUtilLocaleSource.ALL, language))
		allOptions += defaultOption
		allOptions += botInstancesService.instanceDomains.keys.map {
			InstanceOption(it, botInstancesService.createInstanceName(it))
		}
		return OptionsResDto(defaultOption, allOptions)
	}

	override fun getAllInstanceIds() = botInstancesService.instanceDomains.keys.toList()

	override fun getAllInstanceDefinitions(
		avatarSize: Int?,
	) = botInstancesService.instanceDomains.keys.map {
		InstanceDefinitionResDto(
			id = it,
			name = botInstancesService.createInstanceName(it),
			color = botInstancesService.getInstanceColor(it),
			avatarUrl = discordBotApiService.getApplicationAvatarUrl(it, avatarSize),
		)
	}
}
