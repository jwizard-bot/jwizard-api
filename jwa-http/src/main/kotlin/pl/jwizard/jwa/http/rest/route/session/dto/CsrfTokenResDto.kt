package pl.jwizard.jwa.http.rest.route.session.dto

data class CsrfTokenResDto(
	val csrfToken: String,
	val headerName: String,
)
