/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.command

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.springframework.stereotype.Service
import pl.jwizard.api.i18n.I18nService
import pl.jwizard.api.logger.logger
import pl.jwizard.api.network.command.dto.CommandDetailsDto
import pl.jwizard.api.network.command.dto.CommandDetailsMapperDto
import pl.jwizard.api.network.command.dto.CommandsResDto

@Service
class CommandService(
	private val _i18nService: I18nService,
	commandProperties: CommandProperties,
	objectMapper: ObjectMapper,
) : ICommandService {
	private var _botCommands: Map<String, Map<String, CommandDetailsMapperDto>> = emptyMap()

	init {
		_botCommands = objectMapper.readValue(
			commandProperties.resourceFile?.inputStream,
			object : TypeReference<Map<String, Map<String, CommandDetailsMapperDto>>>() {})
	}

	override fun getCommandsBaseLang(): CommandsResDto {
		val categories = _botCommands.mapValues { (key, _) ->
			_i18nService.getMessage("pl.jwizard.command.category.${key}")
		}
		val flattedCommands = _botCommands.flatMap { (category, commandsMap) ->
			commandsMap.map { (name, data) ->
				name to CommandDetailsDto(
					data.aliases.split(","),
					category,
					_i18nService.getMessage("pl.jwizard.command.description.$name")
				)
			}
		}
		return CommandsResDto(categories, flattedCommands.toMap())
	}

	companion object {
		private val _log: Logger = logger<CommandService>()
	}
}
