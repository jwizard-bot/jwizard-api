package pl.jwizard.jwa.service.spi.dto

import java.time.LocalDateTime

data class SessionExpirationState(
	val sessionExpiredAtUtc: LocalDateTime,
	val tokenExpiredAtUtc: LocalDateTime,
	val accessToken: String,
	val refreshToken: String,
)
