package pl.jwizard.jwa.core.server.spi

import java.time.LocalDateTime

data class SessionUser(
	val userSnowflake: Long,
	val accessToken: String,
	val refreshToken: String,
	val tokenExpiredAtUtc: LocalDateTime,
	val sessionExpiredAtUtc: LocalDateTime,
	val csrfToken: String,
)
