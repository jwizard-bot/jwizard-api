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
	EXC_STANDALONE_APP_NOT_EXIST("jwizard.api.exc.standaloneAppNotExist"),
	EXC_REFRESH_TOKEN_NOT_EXIST("jwizard.api.exc.refreshTokenNotExist"),
	EXC_GUILD_NOT_EXIST("jwizard.api.exc.guildNotExist"),

	JWT_INVALID("jwizard.api.jwt.invalid"),
	JWT_EXPIRED("jwizard.api.jwt.expired"),
	;

	override fun getPlaceholder(): String = placeholder
}
