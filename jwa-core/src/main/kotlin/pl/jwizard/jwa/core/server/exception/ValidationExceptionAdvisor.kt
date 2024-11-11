/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.server.exception

import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.validation.ValidationError
import io.javalin.validation.ValidationException
import pl.jwizard.jwa.core.i18n.I18nValidationError
import pl.jwizard.jwl.i18n.I18nBean
import pl.jwizard.jwl.i18n.source.I18nGeneralServerExceptionSource
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.server.attribute.CommonServerAttribute
import pl.jwizard.jwl.server.exception.ExceptionResponse
import pl.jwizard.jwl.server.exception.ExceptionsAdvisorBase
import pl.jwizard.jwl.server.getAttribute
import pl.jwizard.jwl.util.logger

/**
 * Advisor class responsible for handling validation exceptions in HTTP requests.
 *
 * This class intercepts `ValidationException` errors that occur during request validation in Javalin, logs the error,
 * and prepares a structured response with details of each validation error. The error messages are localized based
 * on the user's language settings, which are retrieved from the request context.
 *
 * @property i18nBean Provides localized messages for validation errors, using the application's I18n system.
 * @author Miłosz Gilga
 */
@SingletonComponent
class ValidationExceptionAdvisor(i18nBean: I18nBean) : ExceptionsAdvisorBase<ValidationException>(i18nBean) {

	companion object {
		private val log = logger<ValidationExceptionAdvisor>()
	}

	override val clazz = ValidationException::class

	/**
	 * Handles a [ValidationException] by logging the validation errors and creating a localized response.
	 *
	 * @param ex The [ValidationException] containing validation error details.
	 * @param ctx The Javalin request context, which provides access to request attributes and locale settings.
	 * @return An [ExceptionResponse] containing an HTTP 400 status code, a localized error message, and error details.
	 */
	override fun advise(ex: ValidationException, ctx: Context): ExceptionResponse {
		val language = ctx.getAttribute<String>(CommonServerAttribute.I18N_LOCALE)
		log.info("Validation exception occurs. Cause: {}.", ex.errors)
		return ExceptionResponse(
			status = HttpStatus.BAD_REQUEST,
			i18nLocaleSource = I18nGeneralServerExceptionSource.BAD_REQUEST_EXCEPTION,
			details = ex.errors.mapValues { it.value.map { error -> mapToCommonValidationError(error, language) } }
		)
	}

	/**
	 * Maps a single [ValidationError] to a localized error message.
	 *
	 * This method translates a [ValidationError] into a localized message string based on the user's language setting.
	 * It attempts to use a predefined [I18nValidationError] key for translation, and if it fails, falls back to
	 * the original error message.
	 *
	 * @param error The [ValidationError] to be translated.
	 * @param language The language code used for localization (ex. "en").
	 * @return A localized message string if translation is successful, or the default error message if translation fails.
	 */
	private fun mapToCommonValidationError(error: ValidationError<Any>, language: String?) = try {
		i18nBean.t(I18nValidationError.valueOf(error.message), language, error.args)
	} catch (_: Exception) {
		error.message
	}
}
