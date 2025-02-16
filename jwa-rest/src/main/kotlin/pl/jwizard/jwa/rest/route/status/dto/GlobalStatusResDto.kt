package pl.jwizard.jwa.rest.route.status.dto

data class GlobalStatusResDto(
	val operational: Boolean?,
	val description: String,
	val sourceUrl: String,
)
