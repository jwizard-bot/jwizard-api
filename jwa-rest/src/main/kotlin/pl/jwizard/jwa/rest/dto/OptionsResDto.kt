package pl.jwizard.jwa.rest.dto

data class OptionsResDto<T>(
	val defaultOption: T,
	val options: List<T>,
)
