/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.server

import io.javalin.validation.ValidationError
import io.javalin.validation.ValidationException
import io.javalin.validation.Validator
import pl.jwizard.jwa.core.i18n.I18nValidationError

/**
 * A utility class for chaining validation operations on a given [Validator] instance.
 *
 * This facade allows for the composition of multiple validation checks on a single [Validator] object, enabling fluent
 * chaining of validation rules for cleaner and more readable code. It also supports adding localized error messages by
 * associating validation rules with specific keys from the [I18nValidationError] enum.
 *
 * @param T The type of object being validated.
 * @param validationProvider The initial `Validator` instance provided for the validation chain.
 */
class ValidatorChainFacade<T>(validationProvider: Validator<T>) {

	/**
	 * The underlying [Validator] instance, which holds the current chain of validations.
	 */
	private var validatorChain = validationProvider

	/**
	 * Allows nullable values in the validation chain.
	 *
	 * This method modifies the validator to accept `null` values, bypassing validation checks if the value is null.
	 * @return The current [ValidatorChainFacade] instance, enabling method chaining.
	 */
	fun allowNullables() = apply {
		validatorChain.allowNullable()
	}

	/**
	 * Adds a validation rule to disallow blank values.
	 *
	 * This rule checks if the value, when converted to a string, is not blank. If the value is blank, it triggers
	 * an error with the key [I18nValidationError.NULLCHECK_FAILED] for localization.
	 *
	 * @return The current [ValidatorChainFacade] instance, enabling method chaining.
	 */
	fun disallowBlanks() = apply {
		check({ it.toString().isNotBlank() }, I18nValidationError.NULLCHECK_FAILED)
	}

	/**
	 * Adds a custom validation check based on the provided predicate.
	 *
	 * The predicate is a lambda that performs a validation check on the value. If the predicate returns false,
	 * an error with the specified [i18nLocaleSource] is generated for localization.
	 *
	 * @param predicate A lambda function that returns true if the value is valid, false otherwise.
	 * @param i18nLocaleSource The key from [I18nValidationError] used for generating localized error messages.
	 * @return The current [ValidatorChainFacade] instance, enabling method chaining.
	 */
	fun check(predicate: (T?) -> Boolean, i18nLocaleSource: I18nValidationError) = apply {
		validatorChain.check(predicate, wrapWithException(i18nLocaleSource))
	}

	/**
	 * Finalizes the validation chain and returns the validated value.
	 *
	 * @return The validated value of type `T`.
	 * @throws ValidationException if any validation check fails.
	 */
	fun get() = validatorChain.get()

	/**
	 * Wraps an [I18nValidationError] into a [ValidationError] instance for use in validation checks.
	 *
	 * This method creates a [ValidationError] containing the specified `i18nLocaleSource` key, which is used to retrieve
	 * localized error messages. Additional arguments for error message formatting can be provided via the [args] map.
	 *
	 * @param i18nLocaleSource The [I18nValidationError] key for generating localized error messages.
	 * @param args Additional arguments for error message formatting, passed as a map.
	 * @return A new [ValidationError] instance containing the error key and arguments.
	 */
	private fun <T> wrapWithException(i18nLocaleSource: I18nValidationError, args: Map<String, Any?> = emptyMap()) =
		ValidationError<T>(i18nLocaleSource.name, args)
}
