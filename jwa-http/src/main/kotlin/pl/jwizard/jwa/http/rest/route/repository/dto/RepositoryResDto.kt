package pl.jwizard.jwa.http.rest.route.repository.dto

import java.io.Serializable

data class RepositoryResDto(
	val name: String,
	val slug: String,
	val description: String,
	val link: String,
	val primaryLanguage: PrimaryLanguageDto,
	val lastUpdate: LastUpdateDto,
) : Serializable
