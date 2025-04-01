package pl.jwizard.jwa.gateway.http.rest.route.command.dto

data class CommandArgument(
	val name: String,
	val type: String,
	val required: Boolean,
)
