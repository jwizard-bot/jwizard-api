package pl.jwizard.jwa.core.i18n

import pl.jwizard.jwl.i18n.I18nFragmentSource

enum class I18nAppFragmentSource(override val key: String) : I18nFragmentSource {
	// key features
	KEY_FEATURE_NAME("jwa.feature.%s.heading"),
	KEY_FEATURE_DESCRIPTION("jwa.feature.%s.description"),

	// projects description
	PROJECT_NAME("jwa.project.name.%s"),
	PROJECT_DESCRIPTION("jwa.project.description.%s"),
	;
}
