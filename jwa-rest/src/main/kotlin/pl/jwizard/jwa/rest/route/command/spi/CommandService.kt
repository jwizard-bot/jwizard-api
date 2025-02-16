package pl.jwizard.jwa.rest.route.command.spi

import pl.jwizard.jwa.rest.dto.OptionsResDto
import pl.jwizard.jwa.rest.route.command.dto.CommandDetailsResDto
import pl.jwizard.jwa.rest.route.command.dto.CommandModuleResDto
import pl.jwizard.jwa.rest.route.command.dto.ModuleWithCommandsResDto

interface CommandService {
	fun getModules(language: String?): OptionsResDto<CommandModuleResDto>

	fun getCommands(language: String?): List<ModuleWithCommandsResDto>

	fun getCommandDetails(nameId: String, language: String?): CommandDetailsResDto?
}
