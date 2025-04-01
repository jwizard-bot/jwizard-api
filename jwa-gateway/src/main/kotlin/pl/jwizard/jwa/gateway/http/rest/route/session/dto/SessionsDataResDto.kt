package pl.jwizard.jwa.gateway.http.rest.route.session.dto

data class SessionsDataResDto(
	val current: SessionData,
	val sessions: List<SessionData>,
)
