package pl.jwizard.jwa.service.http

enum class AuthTokenType(val type: String) {
	BOT("Bot"),
	BEARER("Bearer"),
	PLAIN(""),
	;
}
