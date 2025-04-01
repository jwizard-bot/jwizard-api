package pl.jwizard.jwa.gateway.http.rest.route.session.dto

data class SessionData(
	val sessionId: String,
	val lastLoginTime: Long,
	val lastLoginTimeUnit: String,
	val deviceSystem: String?,
	val deviceMobile: Boolean?,
	val geolocationInfo: String?,
)
