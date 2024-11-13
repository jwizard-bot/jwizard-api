/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.i18n

import pl.jwizard.jwl.i18n.I18nLocaleSource

/**
 * Enum class representing localized validation error messages for i18n.
 *
 * @property placeholder The key string used for retrieving the localized value.
 * @author Miłosz Gilga
 */
enum class I18nValidationError(override val placeholder: String) : I18nLocaleSource {
	DESERIALIZATION_FAILED("jwa.validator.deserializedFailed"),
	TYPE_CONVERSION_FAILED("jwa.validator.typeConversionFailed"),
	NULLCHECK_FAILED("jwa.validator.requiredNotNull"),
	;
}
