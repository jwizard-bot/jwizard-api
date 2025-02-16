package pl.jwizard.jwa.core.i18n

import pl.jwizard.jwl.i18n.I18nDynamicSource

enum class I18nServerDynamicSource(override val key: String) : I18nDynamicSource {
	// used in the format jwa.feature.%s.heading where %s is the feature key
	KEY_FEATURE_NAME("jwa.feature.%s.heading"),

	// used in the format jwa.feature.%s.description where %s is the feature key
	KEY_FEATURE_DESCRIPTION("jwa.feature.%s.description"),
	;
}
