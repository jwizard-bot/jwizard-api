/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class MessageExceptionRes(
	val message: String?,
	private val httpStatus: HttpStatus,
	req: HttpServletRequest
) : AbstractServerExceptionResDto(httpStatus, req) {

	fun buildResponseEntity(): ResponseEntity<MessageExceptionRes> = ResponseEntity(this, httpStatus)
}
