package pl.jwizard.jwa.http.rest.route.session.dto

data class RevalidateStateResDto(
	val loggedIn: Boolean,
	val expired: Boolean,
)
