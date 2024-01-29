/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.exception.app

import org.springframework.http.HttpStatus
import pl.jwizard.api.exception.AbstractRestException
import pl.jwizard.api.i18n.ILocaleSet
import pl.jwizard.api.i18n.set.ApiLocaleSet

object IdentityException {
	class StandaloneAppNotExistException(appId: String) : AbstractRestException(
		httpStatus = HttpStatus.NOT_FOUND,
		placeholder = ApiLocaleSet.EXC_STANDALONE_APP_NOT_EXIST,
		clazz = StandaloneAppNotExistException::class,
		logMessage = "Attempt to perform operation via non existing standalone app with ID: $appId"
	)

	class JwtGeneralException(
		httpStatus: HttpStatus, placeholder: ILocaleSet
	) : AbstractRestException(httpStatus, placeholder) {
		constructor() : this(HttpStatus.FORBIDDEN, ApiLocaleSet.JWT_INVALID)
	}
}
