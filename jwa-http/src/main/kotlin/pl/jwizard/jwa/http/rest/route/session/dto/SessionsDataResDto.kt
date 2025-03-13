package pl.jwizard.jwa.http.rest.route.session.dto

data class SessionsDataResDto(
	val current: SessionData,
	val sessions: List<SessionData>,
)
