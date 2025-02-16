package pl.jwizard.jwa.core.i18n

import pl.jwizard.jwl.i18n.I18nLocaleSource

enum class I18nValidationError(override val placeholder: String) : I18nLocaleSource {
	DESERIALIZATION_FAILED("jwa.validator.deserializedFailed"),
	TYPE_CONVERSION_FAILED("jwa.validator.typeConversionFailed"),
	NULLCHECK_FAILED("jwa.validator.requiredNotNull"),
	;
}
