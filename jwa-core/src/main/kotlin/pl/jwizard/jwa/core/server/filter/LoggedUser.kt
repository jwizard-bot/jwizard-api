package pl.jwizard.jwa.core.server.filter

data class LoggedUser(
	val sessionId: String,
	val accessToken: String,
	val userSnowflake: Long,
	val csrfToken: String,
)
