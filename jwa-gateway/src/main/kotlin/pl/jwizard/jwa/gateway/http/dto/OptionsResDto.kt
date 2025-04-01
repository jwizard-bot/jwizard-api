package pl.jwizard.jwa.gateway.http.dto

data class OptionsResDto<T>(
	val defaultOption: T,
	val options: List<T>,
)
