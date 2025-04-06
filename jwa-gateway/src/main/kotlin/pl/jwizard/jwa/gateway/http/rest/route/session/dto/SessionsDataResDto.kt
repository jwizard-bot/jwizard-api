package pl.jwizard.jwa.gateway.http.rest.route.session.dto

data class SessionsDataResDto(
	val current: SessionData,
	val sessions: List<SessionData>,
	val geolocationProviderName: String,
	val geolocationProviderWebsiteUrl: String,
)
