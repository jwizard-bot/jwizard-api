package pl.jwizard.jwa.gateway.http.rest.route.status.dto

data class GlobalStatusResDto(
	val globalUp: Boolean?,
	val externalServicesWebsiteUrl: String,
)
