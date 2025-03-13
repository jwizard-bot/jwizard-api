package pl.jwizard.jwa.service.spi

import pl.jwizard.jwa.core.server.spi.SessionUser
import pl.jwizard.jwa.service.spi.dto.SessionDataRow
import pl.jwizard.jwa.service.spi.dto.SessionExpirationState
import java.time.LocalDateTime

interface SessionSupplier {
	fun getUserSession(sessionId: String): SessionUser?

	fun getMySessions(userSnowflake: Long): List<SessionDataRow>

	fun getSessionEndTime(sessionId: String): LocalDateTime?

	fun saveUserSession(
		sessionId: String,
		userSnowflake: Long,
		accessToken: String,
		refreshToken: String,
		tokenExpiredAtUtc: LocalDateTime,
		sessionExpiredAtUtc: LocalDateTime,
		lastLoginUtc: LocalDateTime,
		deviceSystem: String?,
		deviceMobile: Boolean?,
		geolocationInfo: String?,
		csrfToken: String,
	): Int

	fun updateUserSession(
		sessionId: String,
		sessionExpiredAtUtc: LocalDateTime,
		lastLoginUtc: LocalDateTime,
		deviceSystem: String?,
		deviceMobile: Boolean?,
		geolocationInfo: String?,
		csrfToken: String,
		accessToken: String? = null,
		refreshToken: String? = null,
		tokenExpiredAtUtc: LocalDateTime? = null,
	): Int

	fun updateTokenSession(
		sessionId: String,
		accessToken: String,
		refreshToken: String,
		tokenExpiredAtUtc: LocalDateTime,
	): Int

	fun updateCsrfToken(sessionId: String, encryptedCsrfToken: String): Int

	fun updateSessionTime(sessionId: String, sessionExpiredAtUtc: LocalDateTime): Int

	fun getSessionExpirationState(sessionId: String): SessionExpirationState?

	fun getNonExpiredAccessTokens(sessionId: String, userSnowflake: Long): List<String>

	fun deleteUserSession(userSnowflake: Long, sessionId: String): Int

	fun deleteAllUserSessions(sessionId: String, userSnowflake: Long): Int
}
