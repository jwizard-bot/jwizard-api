package pl.jwizard.jwa.service.discord

data class OAuthTokenData(
	val accessToken: String,
	val refreshToken: String,
	val expiresIn: Long,
)
