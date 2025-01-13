/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.i18n

import pl.jwizard.jwl.i18n.I18nLocaleSource

/**
 * Enum class representing keys used for retrieving localized values from the internationalization (i18n) system. Each
 * enum value corresponds to a specific localization key in the i18n resource files.
 *
 * @property placeholder The key string used for retrieving the localized value.*
 * @author Miłosz Gilga
 */
enum class I18nUtilLocaleSource(override val placeholder: String) : I18nLocaleSource {
	STATUS_OPERATIONAL("jwa.util.status.operational"),
	STATUS_DOWN("jwa.util.status.down"),
	STATUS_UNKNOWN("jwa.util.status.unknown"),
	ALL("jwa.util.all"),
	;
}
