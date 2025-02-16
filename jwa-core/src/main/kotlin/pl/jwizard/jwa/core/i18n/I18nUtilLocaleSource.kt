package pl.jwizard.jwa.core.i18n

import pl.jwizard.jwl.i18n.I18nLocaleSource

enum class I18nUtilLocaleSource(override val placeholder: String) : I18nLocaleSource {
	STATUS_OPERATIONAL("jwa.util.status.operational"),
	STATUS_DOWN("jwa.util.status.down"),
	STATUS_UNKNOWN("jwa.util.status.unknown"),
	ALL("jwa.util.all"),
	;
}
