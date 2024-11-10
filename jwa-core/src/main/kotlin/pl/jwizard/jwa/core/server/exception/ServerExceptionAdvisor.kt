/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.server.exception

import io.javalin.http.Context
import pl.jwizard.jwl.i18n.I18nBean
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.server.exception.ExceptionResponse
import pl.jwizard.jwl.server.exception.ExceptionsAdvisorBase
import pl.jwizard.jwl.util.logger

/**
 * A class responsible for handling server exceptions and generating the appropriate exception response.
 *
 * This class extends the [ExceptionsAdvisorBase] to provide custom behavior for advising on [LocalizedHttpException]
 * instances. When an exception to this type occurs, it logs the error and generates an [ExceptionResponse] that
 * includes details about the error, including its HTTP status, internationalization (i18n) locale, and arguments.
 *
 * @param i18nBean The bean used for internationalization (i18n) purposes.
 * @author Miłosz Gilga
 */
@SingletonComponent
class ServerExceptionAdvisor(i18nBean: I18nBean) : ExceptionsAdvisorBase<LocalizedHttpException>(i18nBean) {

	companion object {
		private val log = logger<ServerExceptionAdvisor>()
	}

	override val clazz = LocalizedHttpException::class

	/**
	 * Handles the exception by logging the error and generating an exception response.
	 *
	 * This method is called when a [LocalizedHttpException] is thrown. It logs the error message and creates an
	 * [ExceptionResponse] that includes the HTTP status, the source for internationalization (i18n), and any arguments
	 * associated with the exception.
	 *
	 * @param ex The exception that was thrown, of type [LocalizedHttpException].
	 * @param ctx The [Context] object for the current request, which is provided by the Javalin framework.
	 * @return An [ExceptionResponse] containing the HTTP status and other relevant information.
	 */
	override fun advise(ex: LocalizedHttpException, ctx: Context): ExceptionResponse {
		log.error("Server controlled exception. Cause: {}.", ex.message)
		return ExceptionResponse(ex.httpStatus, ex.i18nLocaleSource, ex.args)
	}
}
