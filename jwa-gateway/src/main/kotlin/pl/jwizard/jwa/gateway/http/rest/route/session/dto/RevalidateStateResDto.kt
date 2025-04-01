package pl.jwizard.jwa.gateway.http.rest.route.session.dto

data class RevalidateStateResDto(
	val loggedIn: Boolean,
	val expired: Boolean,
)
