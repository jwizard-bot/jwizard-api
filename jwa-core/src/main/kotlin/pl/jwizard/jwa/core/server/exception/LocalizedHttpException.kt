/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.server.exception

import io.javalin.http.HttpResponseException
import io.javalin.http.HttpStatus
import pl.jwizard.jwl.i18n.I18nLocaleSource

/**
 * A base class for exceptions that require localization and HTTP status information.
 *
 * This class extends [HttpResponseException] and adds support for internationalization (i18n) by including a source for
 * localized messages, as well as additional details like the HTTP status code and arguments that can be used to
 * customize the exception message.
 *
 * @property i18nLocaleSource The source for the localized message.
 * @property args A map of arguments that can be used to customize the message.
 * @property httpStatus The HTTP status code associated with the exception. Defaults to [HttpStatus.BAD_REQUEST].
 * @property logMessage A message to log when the exception is thrown.
 * @author Miłosz Gilga
 */
abstract class LocalizedHttpException(
	val i18nLocaleSource: I18nLocaleSource,
	val args: Map<String, Any?> = emptyMap(),
	val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
	private val logMessage: String,
) : HttpResponseException(httpStatus.code, logMessage)
