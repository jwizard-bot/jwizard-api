package pl.jwizard.jwa.service.auth

import nl.basjes.parse.useragent.UserAgent
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerListProperty
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.core.util.UrlSearchParamsBuilder
import pl.jwizard.jwa.http.route.oauth.DiscordOAuthService
import pl.jwizard.jwa.http.route.oauth.dto.LoginResponseData
import pl.jwizard.jwa.service.crypto.EncryptService
import pl.jwizard.jwa.service.crypto.SecureRndGeneratorService
import pl.jwizard.jwa.service.discord.DiscordApiService
import pl.jwizard.jwa.service.spi.SessionSupplier
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.util.logger
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
internal class DiscordOAuthServiceImpl(
	private val discordApiService: DiscordApiService,
	private val environment: BaseEnvironment,
	private val sessionSupplier: SessionSupplier,
	private val encryptService: EncryptService,
	private val secureRndGeneratorService: SecureRndGeneratorService,
	private val geolocationService: GeolocationService,
	private val userAgentAnalyzer: UserAgentAnalyzer,
) : DiscordOAuthService {
	companion object {
		private val log = logger<DiscordOAuthServiceImpl>()
	}

	private val selfUrl = environment.getProperty<String>(ServerProperty.SERVER_SELF_URL)

	private val appId = environment.getProperty<String>(ServerProperty.DISCORD_OAUTH_APP_ID)
	private val scopes = environment.getListProperty<String>(ServerListProperty.DISCORD_OAUTH_SCOPES)
	private val sessionTtlSec = environment
		.getProperty<Int>(ServerProperty.DISCORD_OAUTH_SESSION_TTL_SEC)
	private val sidTokenLength = environment
		.getProperty<Int>(ServerProperty.DISCORD_OAUTH_SID_TOKEN_LENGTH)
	private val csrfTokenLength = environment
		.getProperty<Int>(ServerProperty.DISCORD_OAUTH_CSRF_TOKEN_LENGTH)
	private val cookieDomain = environment
		.getProperty<String>(ServerProperty.DISCORD_OAUTH_COOKIE_DOMAIN)

	override fun generateLoginUrl(basePath: String) = UrlSearchParamsBuilder()
		.setBaseUrl("https://discord.com/oauth2/authorize")
		.addParam("client_id", appId)
		.addParam("response_type", "code")
		.addParam("redirect_uri", makeRedirectUrl(basePath))
		.addParam("scope", scopes.joinToString("+"))
		.build()

	override fun authorize(
		code: String?,
		basePath: String,
		sessionId: String?,
		ipAddress: String?,
		userAgent: String?,
	) = try {
		val csrfToken = encryptService.encrypt(secureRndGeneratorService.generate(csrfTokenLength))
		val sessionTimeAsLong = sessionTtlSec.toLong()
		val geolocationInfo = geolocationService.extractGeolocationBasedIp(ipAddress)

		val analyzerResult = userAgentAnalyzer.parse(userAgent)
		val deviceSystem = analyzerResult.getValue(UserAgent.OPERATING_SYSTEM_NAME)
		val deviceType = analyzerResult.getValue(UserAgent.DEVICE_CLASS)
		val deviceMobile = when (deviceType?.lowercase()) {
			"phone" -> true
			"desktop" -> false
			else -> null
		}
		val now = LocalDateTime.now(ZoneOffset.UTC)

		val noSessionOrSessionExpired = if (sessionId != null) {
			val state = sessionSupplier.getSessionExpirationState(sessionId)
			if (state != null && state.sessionExpiredAtUtc.isAfter(now)) {
				var newAccessToken: String? = null
				var newRefreshToken: String? = null
				var tokenExpiredAtUtc: LocalDateTime? = null
				if (state.tokenExpiredAtUtc.isBefore(now)) {
					val decryptedRefreshToken = encryptService.decrypt(state.refreshToken)
					val tokenData = discordApiService.refreshOAuthToken(decryptedRefreshToken)
					newAccessToken = encryptService.encrypt(tokenData.accessToken)
					newRefreshToken = encryptService.encrypt(tokenData.refreshToken)
					tokenExpiredAtUtc = now.plusSeconds(tokenData.expiresIn)
					log.debug("Refresh access token for session ID: \"{}\".", sessionId)
				}
				sessionSupplier.updateUserSession(
					sessionId,
					sessionExpiredAtUtc = now.plusSeconds(sessionTimeAsLong),
					lastLoginUtc = now,
					deviceSystem = deviceSystem,
					deviceMobile = deviceMobile,
					geolocationInfo = geolocationInfo,
					csrfToken = csrfToken,
					accessToken = newAccessToken,
					refreshToken = newRefreshToken,
					tokenExpiredAtUtc = tokenExpiredAtUtc,
				)
				log.debug("Update existing session state with session ID: \"{}\".", sessionId)
				false
			} else {
				true
			}
		} else {
			true
		}
		val persistedSid = if (noSessionOrSessionExpired) {
			val tokenData = discordApiService.generateOAuthToken(
				code.toString(),
				makeRedirectUrl(basePath),
			)
			val userData = discordApiService.performDiscordApiRequest(
				urlSuffix = "/users/@me",
				authToken = tokenData.accessToken,
			)
			val newlySessionId = secureRndGeneratorService.generate(sidTokenLength)
			val userId = userData.get("id").asLong()
			sessionSupplier.saveUserSession(
				sessionId = newlySessionId,
				userSnowflake = userId,
				accessToken = encryptService.encrypt(tokenData.accessToken),
				refreshToken = encryptService.encrypt(tokenData.refreshToken),
				tokenExpiredAtUtc = now.plusSeconds(tokenData.expiresIn),
				sessionExpiredAtUtc = now.plusSeconds(sessionTimeAsLong),
				lastLoginUtc = now,
				deviceSystem = deviceSystem,
				deviceMobile = deviceMobile,
				geolocationInfo = geolocationInfo,
				csrfToken = csrfToken,
			)
			log.debug("Persist new session for user snowflake: \"{}\".", userId)
			newlySessionId
		} else {
			sessionId
		}
		LoginResponseData(
			redirectUrl = environment.getProperty(ServerProperty.DISCORD_OAUTH_REDIRECT_URL_SUCCESS),
			sessionId = persistedSid,
			domain = cookieDomain,
			sessionTtl = sessionTtlSec,
		)
	} catch (ex: Exception) {
		// catch all errors, because we must redirect to front-end (we do not returning json object)
		val errorDigitCode = secureRndGeneratorService.generateDigitCode(12)
		log.error(
			"({}) Unable to log in user via Discord OAuth provider. Cause: \"{}\".",
			errorDigitCode, ex.message,
		)
		val baseUrl = environment.getProperty<String>(ServerProperty.DISCORD_OAUTH_REDIRECT_URL_ERROR)
		LoginResponseData(baseUrl.format(errorDigitCode), domain = cookieDomain)
	}

	private fun makeRedirectUrl(basePath: String) = "$selfUrl$basePath/discord/redirect"
}
