package pl.jwizard.jwa.rest.route.command.dto

data class CommandArgument(
	val name: String,
	val type: String,
	val required: Boolean,
)
