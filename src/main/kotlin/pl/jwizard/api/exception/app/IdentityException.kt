/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.exception.app

import org.springframework.http.HttpStatus
import pl.jwizard.api.exception.AbstractRestException
import pl.jwizard.api.i18n.LocaleSet
import pl.jwizard.api.i18n.set.ApiLocaleSet

object IdentityException {
	class StandaloneAppNotExistException(appId: String) : AbstractRestException(
		httpStatus = HttpStatus.NOT_FOUND,
		placeholder = ApiLocaleSet.EXC_STANDALONE_APP_NOT_EXIST,
		clazz = StandaloneAppNotExistException::class,
		logMessage = "Attempt to perform operation via non existing standalone app with ID: $appId"
	)

	class RefreshTokenNotExistException(refreshToken: String, appId: String) : AbstractRestException(
		httpStatus = HttpStatus.NOT_FOUND,
		placeholder = ApiLocaleSet.EXC_REFRESH_TOKEN_NOT_EXIST,
		clazz = RefreshTokenNotExistException::class,
		logMessage = "Attempt to perform operation via invalid refresh token $refreshToken for: $appId"
	)

	class JwtGeneralException(
		httpStatus: HttpStatus, placeholder: LocaleSet
	) : AbstractRestException(httpStatus, placeholder) {
		constructor() : this(HttpStatus.FORBIDDEN, ApiLocaleSet.JWT_INVALID)
	}
}
