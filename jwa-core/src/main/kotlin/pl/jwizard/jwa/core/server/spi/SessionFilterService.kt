package pl.jwizard.jwa.core.server.spi

import java.time.LocalDateTime

interface SessionFilterService {
	fun getUserSession(sessionId: String): SessionUser?

	// return refreshed decrypted access token and updated session time
	fun refreshDiscordAccessToken(
		sessionId: String,
		encryptedRefreshToken: String,
		now: LocalDateTime,
	): Pair<Int, String?>

	// return updated session time
	fun updateSessionTime(sessionId: String, now: LocalDateTime): Int

	fun deleteExpiredSession(sessionId: String, userSnowflake: Long)

	fun decryptToken(encryptedToken: String): String
}
