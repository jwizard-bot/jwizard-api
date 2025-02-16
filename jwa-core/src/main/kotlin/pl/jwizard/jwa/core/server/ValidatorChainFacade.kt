package pl.jwizard.jwa.core.server

import io.javalin.validation.ValidationError
import io.javalin.validation.Validator
import pl.jwizard.jwa.core.i18n.I18nValidationError

class ValidatorChainFacade<T>(validationProvider: Validator<T>) {
	private var validatorChain = validationProvider

	fun allowNullables() = apply {
		validatorChain.allowNullable()
	}

	fun disallowBlanks() = apply {
		check({ it.toString().isNotBlank() }, I18nValidationError.NULLCHECK_FAILED)
	}

	fun check(predicate: (T?) -> Boolean, i18nLocaleSource: I18nValidationError) = apply {
		validatorChain.check(predicate, wrapWithException(i18nLocaleSource))
	}

	fun get() = validatorChain.get()

	private fun <T> wrapWithException(
		i18nLocaleSource: I18nValidationError,
		args: Map<String, Any?> = emptyMap(),
	) = ValidationError<T>(i18nLocaleSource.name, args)
}
