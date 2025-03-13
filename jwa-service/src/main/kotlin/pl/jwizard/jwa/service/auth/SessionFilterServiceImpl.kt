package pl.jwizard.jwa.service.auth

import okio.IOException
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.core.server.spi.SessionFilterService
import pl.jwizard.jwa.service.crypto.EncryptService
import pl.jwizard.jwa.service.discord.DiscordApiService
import pl.jwizard.jwa.service.spi.SessionSupplier
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.util.logger
import java.time.LocalDateTime

@Component
internal class SessionFilterServiceImpl(
	private val sessionSupplier: SessionSupplier,
	private val encryptService: EncryptService,
	private val discordApiService: DiscordApiService,
	environment: BaseEnvironment,
) : SessionFilterService {
	companion object {
		private val log = logger<SessionFilterServiceImpl>()
	}

	private val sessionTtlSec = environment
		.getProperty<Int>(ServerProperty.DISCORD_OAUTH_SESSION_TTL_SEC)

	override fun getUserSession(sessionId: String) = sessionSupplier.getUserSession(sessionId)

	override fun refreshDiscordAccessToken(
		sessionId: String,
		encryptedRefreshToken: String,
		now: LocalDateTime,
	) = try {
		val refreshToken = encryptService.decrypt(encryptedRefreshToken)
		val tokenData = discordApiService.refreshOAuthToken(refreshToken)
		sessionSupplier.updateTokenSession(
			sessionId,
			accessToken = encryptService.encrypt(tokenData.accessToken),
			refreshToken = encryptService.encrypt(tokenData.refreshToken),
			tokenExpiredAtUtc = now.plusSeconds(tokenData.expiresIn),
		)
		log.debug("Refresh access token for session ID: \"{}\".", sessionId)
		Pair(sessionTtlSec, tokenData.accessToken)
	} catch (ex: IOException) {
		Pair(sessionTtlSec, null)
	}

	override fun updateSessionTime(sessionId: String, now: LocalDateTime): Int {
		sessionSupplier.updateSessionTime(sessionId, now.plusSeconds(sessionTtlSec.toLong()))
		log.debug("Update session time for session ID: \"{}\".", sessionId)
		return sessionTtlSec
	}

	override fun deleteExpiredSession(sessionId: String, userSnowflake: Long) {
		sessionSupplier.deleteUserSession(userSnowflake, sessionId)
		log.debug("Delete expired session from user snowflake: \"{}\".", userSnowflake)
	}

	override fun decryptToken(encryptedToken: String) = encryptService.decrypt(encryptedToken)
}
