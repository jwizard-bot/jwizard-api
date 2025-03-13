package pl.jwizard.jwa.service.spi.dto

import java.time.LocalDateTime

data class SessionDataRow(
	val sessionId: ByteArray,
	val lastLoginUtc: LocalDateTime,
	val deviceSystem: String?,
	val deviceMobile: Boolean?,
	val geolocationInfo: String?,
)
