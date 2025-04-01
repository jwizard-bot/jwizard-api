package pl.jwizard.jwa.gateway.http.rest.route.session.dto

data class CsrfTokenResDto(
	val csrfToken: String,
	val headerName: String,
)
