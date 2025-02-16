package pl.jwizard.jwa.core.server.exception

import io.javalin.http.HttpResponseException
import io.javalin.http.HttpStatus
import pl.jwizard.jwl.i18n.I18nLocaleSource

abstract class LocalizedHttpException(
	val i18nLocaleSource: I18nLocaleSource,
	val args: Map<String, Any?> = emptyMap(),
	val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
	logMessage: String,
) : HttpResponseException(httpStatus.code, logMessage)
