package pl.jwizard.jwa.gateway.http.rest.route.repository.dto

import java.io.Serializable
import java.time.LocalDateTime

data class LastUpdateDto(
	val buildSha: String,
	val buildDate: LocalDateTime,
	val link: String,
) : Serializable
