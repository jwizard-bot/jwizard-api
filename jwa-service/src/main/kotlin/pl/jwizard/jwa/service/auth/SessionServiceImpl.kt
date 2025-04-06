package pl.jwizard.jwa.service.auth

import io.javalin.http.UnauthorizedResponse
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.core.server.ApiHttpHeader
import pl.jwizard.jwa.core.server.filter.LoggedUser
import pl.jwizard.jwa.core.util.BlockingThreadsExecutor
import pl.jwizard.jwa.gateway.http.rest.route.session.SessionService
import pl.jwizard.jwa.gateway.http.rest.route.session.dto.CsrfTokenResDto
import pl.jwizard.jwa.gateway.http.rest.route.session.dto.RevalidateStateResDto
import pl.jwizard.jwa.gateway.http.rest.route.session.dto.SessionData
import pl.jwizard.jwa.gateway.http.rest.route.session.dto.SessionsDataResDto
import pl.jwizard.jwa.service.crypto.EncryptService
import pl.jwizard.jwa.service.crypto.SecureRndGeneratorService
import pl.jwizard.jwa.service.discord.DiscordApiService
import pl.jwizard.jwa.service.spi.SessionSupplier
import pl.jwizard.jwa.service.spi.dto.SessionDataRow
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.server.useragent.GeolocationProvider
import pl.jwizard.jwl.util.base64encode
import pl.jwizard.jwl.util.logger
import pl.jwizard.jwl.util.timeDifference
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
internal class SessionServiceImpl(
	private val sessionSupplier: SessionSupplier,
	private val discordApiService: DiscordApiService,
	private val encryptService: EncryptService,
	private val secureRndGeneratorService: SecureRndGeneratorService,
	private val geolocationProvider: GeolocationProvider,
	environment: BaseEnvironment,
) : SessionService {
	companion object {
		private val log = logger<SessionServiceImpl>()
	}

	private val csrfTokenLength = environment
		.getProperty<Int>(ServerProperty.DISCORD_OAUTH_CSRF_TOKEN_LENGTH)

	override fun mySessions(loggedUser: LoggedUser): SessionsDataResDto {
		val sessions = sessionSupplier.getMySessions(loggedUser.userSnowflake)
		val currentSession = sessions
			.find { base64encode(it.sessionId) == loggedUser.sessionId }
			?: throw UnauthorizedResponse()

		val now = LocalDateTime.now(ZoneOffset.UTC)
		val objectMapper: (SessionDataRow) -> SessionData = {
			val (lastLoginUtcUnit, lastLoginUtcTime) = timeDifference(it.lastLoginUtc, now)
			SessionData(
				sessionId = base64encode(it.sessionId),
				lastLoginTime = lastLoginUtcTime,
				lastLoginTimeUnit = lastLoginUtcUnit,
				deviceSystem = it.deviceSystem,
				deviceMobile = it.deviceMobile,
				geolocationInfo = it.geolocationInfo,
			)
		}
		val (name, url) = geolocationProvider.geolocationApiInfo
		return SessionsDataResDto(
			current = objectMapper(currentSession),
			sessions = sessions
				.sortedBy(SessionDataRow::lastLoginUtc)
				.filter { base64encode(it.sessionId) != loggedUser.sessionId }
				.map(objectMapper),
			geolocationProviderName = name,
			geolocationProviderWebsiteUrl = url,
		)
	}

	override fun deleteMySessionBasedSessionId(
		toDeleteSessionId: String,
		loggedUser: LoggedUser,
	): Boolean {
		// we cannot delete current session
		if (toDeleteSessionId.contentEquals(loggedUser.sessionId)) {
			return false
		}
		val state = sessionSupplier.getSessionExpirationState(toDeleteSessionId) ?: return false
		val now = LocalDateTime.now(ZoneOffset.UTC)
		if (state.sessionExpiredAtUtc.isAfter(now)) {
			if (state.tokenExpiredAtUtc.isAfter(now)) {
				// revoke access token only when access token is still valid
				discordApiService.revokeOAuthToken(state.accessToken)
				log.debug("Revoke OAuth token for session ID: \"{}\".", toDeleteSessionId)
			}
			sessionSupplier.deleteUserSession(loggedUser.userSnowflake, toDeleteSessionId)
			log.debug("Delete user session for session ID: \"{}\".", toDeleteSessionId)
			return true
		}
		return false
	}

	override fun deleteAllMySessionsWithoutCurrentSession(loggedUser: LoggedUser) {
		val sessionId = loggedUser.sessionId
		val snowflake = loggedUser.userSnowflake
		val accessTokens = sessionSupplier.getNonExpiredAccessTokens(sessionId, snowflake)
		val blockingThreadsExecutor = BlockingThreadsExecutor(
			poolSize = accessTokens.size,
			futureCallback = { accessToken: String -> discordApiService.revokeOAuthToken(accessToken) }
		)
		blockingThreadsExecutor.initThreads(accessTokens)
		val revoked = blockingThreadsExecutor.waitAndGet()
		log.debug("Revoked: {} access tokens for user snowflake: \"{}\".", revoked, snowflake)
		val deleted = sessionSupplier.deleteAllUserSessions(sessionId, snowflake)
		log.debug("Delete: {} user sessions for user snowflake: \"{}\".", deleted, snowflake)
	}

	override fun updateAndGetCsrfToken(sessionId: String): CsrfTokenResDto {
		val csrfToken = encryptService.encrypt(secureRndGeneratorService.generate(csrfTokenLength))
		sessionSupplier.updateCsrfToken(sessionId, csrfToken)
		return CsrfTokenResDto(csrfToken, ApiHttpHeader.X_CSRF_TOKEN.headerName)
	}

	override fun revalidate(sessionId: String?): RevalidateStateResDto {
		var loggedIn = false
		var expired = false
		if (sessionId != null) {
			val sessionExpirationTime = sessionSupplier.getSessionEndTime(sessionId)
			if (sessionExpirationTime != null) {
				val now = LocalDateTime.now(ZoneOffset.UTC)
				loggedIn = sessionExpirationTime.isAfter(now) || sessionExpirationTime.isEqual(now)
				expired = sessionExpirationTime.isBefore(now)
			}
		}
		return RevalidateStateResDto(loggedIn, expired)
	}

	// return false, if revoked is not succeeded
	override fun logout(loggedUser: LoggedUser): Boolean {
		discordApiService.revokeOAuthToken(loggedUser.accessToken)
		sessionSupplier.deleteUserSession(loggedUser.userSnowflake, loggedUser.sessionId)
		log.debug(
			"Revoke access token and delete session from user snowflake: \"{}\".",
			loggedUser.userSnowflake,
		)
		return true
	}
}
