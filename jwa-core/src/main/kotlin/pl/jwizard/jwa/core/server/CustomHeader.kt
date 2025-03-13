package pl.jwizard.jwa.core.server

import io.javalin.http.Context

enum class CustomHeader(val headerName: String) {
	CSRF_TOKEN("X-CSRF-TOKEN"),
	FORWARDED_FOR("X-FORWARDED-FOR"),
	;

	companion object {
		fun Context.header(customHeader: CustomHeader) = header(customHeader.headerName)
	}
}
