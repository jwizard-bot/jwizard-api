package pl.jwizard.jwa.rest.route.command.dto

data class CommandData(
	val name: String,
	val slug: String,
	val description: String,
	val arguments: List<CommandArgument>,
	val legacyUsage: List<String>,
	val slashUsage: List<String>?,
)
