/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.i18n

import pl.jwizard.jwa.core.i18n.I18nServerDynamicMod.KEY_FEATURE_DESCRIPTION
import pl.jwizard.jwa.core.i18n.I18nServerDynamicMod.KEY_FEATURE_NAME
import pl.jwizard.jwl.i18n.I18nDynamicModule

/**
 * Enum class defining dynamic internationalization (i18n) keys for the server.
 *
 * Defining following properties:
 * - [KEY_FEATURE_NAME]: Key for the feature name.
 * - [KEY_FEATURE_DESCRIPTION]: Key for the feature description.
 *
 * @property key The key string used for retrieving the localized value.
 * @author Miłosz Gilga
 */
enum class I18nServerDynamicMod(override val key: String) : I18nDynamicModule {

	/**
	 * Key for the feature name, used in the format `jwa.feature.%s.heading`.
	 */
	KEY_FEATURE_NAME("jwa.feature.%s.heading"),

	/**
	 * Key for the feature description, used in the format `jwa.feature.%s.description`.
	 */
	KEY_FEATURE_DESCRIPTION("jwa.feature.%s.description"),
	;
}
