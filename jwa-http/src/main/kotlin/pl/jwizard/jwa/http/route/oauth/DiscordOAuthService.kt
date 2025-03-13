package pl.jwizard.jwa.http.route.oauth

import pl.jwizard.jwa.http.route.oauth.dto.LoginResponseData

interface DiscordOAuthService {
	fun generateLoginUrl(redirectUrl: String?): String

	fun authorize(
		code: String?,
		redirectUrl: String?,
		sessionId: String?,
		ipAddress: String,
		userAgent: String?,
	): LoginResponseData
}
