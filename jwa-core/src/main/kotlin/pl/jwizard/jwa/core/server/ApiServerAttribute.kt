package pl.jwizard.jwa.core.server

import pl.jwizard.jwl.server.ServerAttribute

enum class ApiServerAttribute(override val attributeId: String) : ServerAttribute {
	I18N_LOCALE("i18n_locale"),
	AUTHENTICATED_USER("authenticated_user"),
	;
}
