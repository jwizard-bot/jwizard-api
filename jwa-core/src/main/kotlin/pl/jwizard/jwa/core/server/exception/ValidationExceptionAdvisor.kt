package pl.jwizard.jwa.core.server.exception

import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.validation.ValidationError
import io.javalin.validation.ValidationException
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.i18n.I18nValidationError
import pl.jwizard.jwa.core.server.ApiServerAttribute
import pl.jwizard.jwl.i18n.I18n
import pl.jwizard.jwl.server.exception.ExceptionAdvisor
import pl.jwizard.jwl.server.getAttribute
import pl.jwizard.jwl.util.logger

@Component
class ValidationExceptionAdvisor(private val i18n: I18n) : ExceptionAdvisor<ValidationException> {
	companion object {
		private val log = logger<ValidationExceptionAdvisor>()
	}

	override val clazz = ValidationException::class
	override fun executeStatement(ex: ValidationException, ctx: Context) {
		val language = ctx.getAttribute<String>(ApiServerAttribute.I18N_LOCALE)
		val errors = ex.errors.mapValues {
			it.value.map { error -> mapToCommonValidationError(error, language) }
		}
		log.info("Validation exception occurs. Cause: {}.", ex.errors)
		ctx.status(HttpStatus.BAD_REQUEST).json(errors)
	}

	private fun mapToCommonValidationError(error: ValidationError<Any>, language: String?) = try {
		i18n.t(I18nValidationError.valueOf(error.message), language, error.args)
	} catch (_: Exception) {
		error.message
	}
}
