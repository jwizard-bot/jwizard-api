package pl.jwizard.jwa.http.route.oauth

import pl.jwizard.jwa.http.route.oauth.dto.LoginResponseData

interface DiscordOAuthService {
	fun generateLoginUrl(basePath: String): String

	fun authorize(
		code: String?,
		basePath: String,
		sessionId: String?,
		ipAddress: String,
		userAgent: String?,
	): LoginResponseData
}
