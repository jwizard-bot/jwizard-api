package pl.jwizard.jwa.http.rest.route.command

import pl.jwizard.jwa.http.dto.OptionsResDto
import pl.jwizard.jwa.http.rest.route.command.dto.CommandDetailsResDto
import pl.jwizard.jwa.http.rest.route.command.dto.CommandModuleResDto
import pl.jwizard.jwa.http.rest.route.command.dto.ModuleWithCommandsResDto

interface CommandService {
	fun getModules(language: String?): OptionsResDto<CommandModuleResDto>

	fun getCommands(language: String?): List<ModuleWithCommandsResDto>

	fun getCommandDetails(nameId: String, language: String?): CommandDetailsResDto?
}
