/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.command

import org.springframework.stereotype.Service
import pl.jwizard.api.i18n.I18nProperties
import pl.jwizard.api.i18n.I18nService
import pl.jwizard.api.network.command.dto.CommandArgumentDto
import pl.jwizard.api.network.command.dto.CommandDetailsDto
import pl.jwizard.api.network.command.dto.CommandsResDto
import pl.jwizard.api.network.guild.GuildDefaultProperties
import pl.jwizard.api.scaffold.AbstractLoggingBean
import java.util.*

@Service
class CommandServiceImpl(
	private val i18nService: I18nService,
	private val i18nProperties: I18nProperties,
	private val commandProperties: CommandProperties,
	private val guildDefaultProperties: GuildDefaultProperties,
) : CommandService, AbstractLoggingBean(CommandServiceImpl::class) {

	override fun getCommandsBaseLang(): Map<String, CommandsResDto> {
		val allLocales = i18nProperties.availableLocales.associateWith { Locale.forLanguageTag(it) }
		val createCommandsForLanguage: (locale: Locale) -> CommandsResDto = { locale ->
			CommandsResDto(
				categories = CommandCategory.entries.associate {
					it.regularName to i18nService.getMessage("pl.jwizard.command.category.${it.regularName}", locale)
				},
				commmands = CommandCategory.entries
					.map { it.commandSupplier(commandProperties) }
					.flatMap { (category, commands) -> commands.map { mapToCommandDetails(it, category, locale) } }
					.toMap(),
				modules = guildDefaultProperties.modules
					.associateWith { i18nService.getMessage("pl.jwizard.command.module.$it") }
			)
		}
		return allLocales.mapValues { createCommandsForLanguage(it.value) }
	}

	private fun mapToCommandDetails(
		command: Command,
		category: String,
		locale: Locale
	): Pair<String, CommandDetailsDto> = command.name to CommandDetailsDto(
		aliases = command.aliases.split(","),
		category = category,
		desc = i18nService.getMessage(
			"pl.jwizard.command.description.${command.name}",
			locale
		),
		argsDesc = if (command.arguments.isNotEmpty()) i18nService.getMessage(
			"pl.jwizard.command.argument.description.${command.name}",
			locale
		) else null,
		args = command.arguments.map { (argName, castedType, required) ->
			CommandArgumentDto(
				id = argName,
				name = i18nService.getMessage("pl.jwizard.command.argument.${argName}", locale),
				type = castedType,
				req = required,
			)
		}
	)
}
