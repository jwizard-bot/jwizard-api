package pl.jwizard.jwa.http.route.oauth.dto

data class LoginResponseData(
	val redirectUrl: String,
	val sessionId: String? = null,
	val sessionTtl: Int = 0,
)
