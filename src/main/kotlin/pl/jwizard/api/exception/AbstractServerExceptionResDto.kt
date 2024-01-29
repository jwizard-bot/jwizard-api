/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import pl.jwizard.api.util.DateUtil

abstract class AbstractServerExceptionResDto(
	val timestamp: String,
	val status: Int,
	val path: String,
	val method: String
) {
	constructor(httpStatus: HttpStatus, req: HttpServletRequest) : this(
		DateUtil.nowUtcToIsoInstant(),
		httpStatus.value(),
		req.servletPath,
		req.method
	)
}
