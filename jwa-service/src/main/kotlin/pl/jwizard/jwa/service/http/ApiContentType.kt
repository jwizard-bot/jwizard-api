package pl.jwizard.jwa.service.http

import io.javalin.http.ContentType

enum class ApiContentType(val mime: String) {
	APPLICATION_JSON(ContentType.APPLICATION_JSON.mimeType),
	WWW_FORM_URL_ENCODED("application/x-www-form-urlencoded"),
	;
}
