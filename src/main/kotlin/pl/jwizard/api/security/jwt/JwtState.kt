/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security.jwt

import pl.jwizard.api.i18n.ILocaleSet
import pl.jwizard.api.i18n.set.ApiLocaleSet

enum class JwtState(val placeholder: ILocaleSet?) {
	VALID(null),
	INVALID(ApiLocaleSet.JWT_INVALID),
	EXPIRED(ApiLocaleSet.JWT_EXPIRED),
	;
}
