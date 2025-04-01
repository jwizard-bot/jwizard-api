package pl.jwizard.jwa.gateway.http.rest.route.command.dto

data class CommandDetailsResDto(
	val name: String,
	val description: String,
	val arguments: List<CommandArgument>,
	val legacyUsage: List<String>,
	val slashUsage: List<String>?,
)
