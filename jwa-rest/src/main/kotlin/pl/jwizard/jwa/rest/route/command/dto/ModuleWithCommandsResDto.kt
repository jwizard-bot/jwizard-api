package pl.jwizard.jwa.rest.route.command.dto

data class ModuleWithCommandsResDto(
	val id: String,
	val name: String,
	val commands: List<CommandData>,
)
