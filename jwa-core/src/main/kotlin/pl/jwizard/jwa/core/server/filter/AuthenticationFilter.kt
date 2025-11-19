package pl.jwizard.jwa.core.server.filter

import io.javalin.http.Context
import io.javalin.http.UnauthorizedResponse
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.core.server.ApiServerAttribute
import pl.jwizard.jwa.core.server.Role
import pl.jwizard.jwa.core.server.ServerCookie
import pl.jwizard.jwa.core.server.ServerCookie.Companion.cookie
import pl.jwizard.jwa.core.server.ServerCookie.Companion.removeCookie
import pl.jwizard.jwa.core.server.spi.SessionFilterService
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.server.filter.FilterRole
import pl.jwizard.jwl.server.filter.RoleFilterBase
import pl.jwizard.jwl.server.setAttribute
import pl.jwizard.jwl.util.logger
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class AuthenticationFilter(
	private val sessionFilterService: SessionFilterService,
	environment: BaseEnvironment,
) : RoleFilterBase() {
	companion object {
		private val log = logger<AuthenticationFilter>()
	}

	override val roles = arrayOf<FilterRole>(Role.AUTHENTICATED)
	override val runIndex = 1

	private val cookieDomain = environment
		.getProperty<String>(ServerProperty.DISCORD_OAUTH_COOKIE_DOMAIN)

	override fun roleFilter(ctx: Context) {
		val sessionId = ctx.cookie(ServerCookie.SID) ?: throw UnauthorizedResponse()
		val session = sessionFilterService.getUserSession(sessionId)
		if (session == null) {
			ctx.removeCookie(ServerCookie.SID, cookieDomain)
			log.debug("Not found active session with session ID: \"{}\". Delete cookie.", sessionId)
			throw UnauthorizedResponse()
		}
		val now = LocalDateTime.now(ZoneOffset.UTC)

		// if session expired, remove it from db and return 401
		if (session.sessionExpiredAtUtc.isBefore(now)) {
			sessionFilterService.deleteExpiredSession(sessionId, session.userSnowflake)
			ctx.removeCookie(ServerCookie.SID, cookieDomain)
			log.debug("Session with session ID: \"{}\" is expired. Delete cookie.", sessionId)
			throw UnauthorizedResponse()
		}
		// if discord access token is expired, refresh it
		val (sessionTime, accessToken) = if (session.tokenExpiredAtUtc.isBefore(now)) {
			sessionFilterService.refreshDiscordAccessToken(sessionId, session.refreshToken, now)
		} else {
			val time = sessionFilterService.updateSessionTime(sessionId, now)
			log.debug("Update session time for  session ID: \"{}\".", sessionId)
			Pair(time, sessionFilterService.decryptToken(session.accessToken))
		}
		if (accessToken == null) {
			throw UnauthorizedResponse()
		}
		val updatedCookie = ServerCookie.SID.toCookieInstance(
			value = sessionId,
			ttl = sessionTime,
			domain = cookieDomain,
			httpOnly = true,
			secure = true,
		)
		val loggedUser = LoggedUser(
			sessionId,
			accessToken,
			userSnowflake = session.userSnowflake,
			csrfToken = session.csrfToken,
		)
		ctx.setAttribute(ApiServerAttribute.AUTHENTICATED_USER, loggedUser)
		ctx.cookie(updatedCookie)
	}
}
