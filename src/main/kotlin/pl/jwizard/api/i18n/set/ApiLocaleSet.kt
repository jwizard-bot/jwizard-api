/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.i18n.set

import pl.jwizard.api.i18n.LocaleSet

enum class ApiLocaleSet(private val placeholder: String) : LocaleSet {
	EXC_UNKNOW_SERVER_EXCEPTION("jwizard.api.exc.unknowServerException"),
	EXC_AUTHENTICATION_EXCEPTION("jwizard.api.exc.authenticationException"),
	EXC_MISSING_REQUEST_PARAMETER("jwizard.api.exc.missingRequestParameter"),
	;

	override fun getPlaceholder(): String = placeholder
}
