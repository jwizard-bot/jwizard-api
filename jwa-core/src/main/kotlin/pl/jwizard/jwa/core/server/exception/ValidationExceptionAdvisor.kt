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

@SingletonComponent
class ValidationExceptionAdvisor(
	i18nBean: I18nBean,
) : ExceptionsAdvisorBase<ValidationException>(i18nBean) {
	companion object {
		private val log = logger<ValidationExceptionAdvisor>()
	}

	override val clazz = ValidationException::class

	override fun advise(ex: ValidationException, ctx: Context): ExceptionResponse {
		val language = ctx.getAttribute<String>(CommonServerAttribute.I18N_LOCALE)
		log.info("Validation exception occurs. Cause: {}.", ex.errors)
		return ExceptionResponse(
			status = HttpStatus.BAD_REQUEST,
			i18nLocaleSource = I18nGeneralServerExceptionSource.BAD_REQUEST_EXCEPTION,
			details = ex.errors.mapValues {
				it.value.map { error ->
					mapToCommonValidationError(
						error,
						language,
					)
				}
			}
		)
	}

	private fun mapToCommonValidationError(error: ValidationError<Any>, language: String?) = try {
		i18nBean.t(I18nValidationError.valueOf(error.message), language, error.args)
	} catch (_: Exception) {
		error.message
	}
}
