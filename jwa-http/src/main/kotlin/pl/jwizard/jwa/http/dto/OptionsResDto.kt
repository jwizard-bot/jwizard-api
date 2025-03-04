package pl.jwizard.jwa.http.dto

data class OptionsResDto<T>(
	val defaultOption: T,
	val options: List<T>,
)
