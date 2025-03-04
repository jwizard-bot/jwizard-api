package pl.jwizard.jwa.http.rest.route.repository.dto

import java.io.Serializable

data class PrimaryLanguageDto(
	val name: String,
	val color: String?,
) : Serializable
