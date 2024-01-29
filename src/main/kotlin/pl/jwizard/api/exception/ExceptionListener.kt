/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pl.jwizard.api.i18n.I18nService
import pl.jwizard.api.i18n.set.ApiLocaleSet
import pl.jwizard.api.scaffold.AbstractLoggingBean

@RestControllerAdvice
class ExceptionListener(
	private val i18nService: I18nService,
) : AbstractLoggingBean(ExceptionListener::class) {

	@ExceptionHandler(AbstractRestException::class)
	fun restException(
		ex: AbstractRestException,
		req: HttpServletRequest
	): ResponseEntity<MessageExceptionRes> {
		val message = i18nService.getMessage(ex.placeholder, ex.variables)
		return MessageExceptionRes(message, ex.httpStatus, req).buildResponseEntity()
	}

	@ExceptionHandler(MissingServletRequestParameterException::class)
	fun missingQueryParameter(
		ex: MissingServletRequestParameterException,
		req: HttpServletRequest,
	): ResponseEntity<MessageExceptionRes> {
		val message = i18nService.getMessage(
			ApiLocaleSet.EXC_MISSING_REQUEST_PARAMETER,
			mapOf(Pair("parameter", "${ex.parameterName}:${ex.parameterType}"))
		)
		return MessageExceptionRes(message, HttpStatus.BAD_REQUEST, req).buildResponseEntity()
	}

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun methodArgumentNotValid(
		ex: MethodArgumentNotValidException,
		req: HttpServletRequest,
	): ResponseEntity<out AbstractServerExceptionResDto> {
		val errors = ex.bindingResult.fieldErrors
		val responseStatus = HttpStatus.BAD_REQUEST
		if (errors.isEmpty()) {
			val error = ex.bindingResult.allErrors[0]
			return MessageExceptionRes(
				message = i18nService.getMessage(error.defaultMessage ?: ""),
				httpStatus = responseStatus,
				req
			).buildResponseEntity()
		}
		val errorsAsMap = errors.associate { it.field to i18nService.getMessage(it.defaultMessage ?: "") }
		return ValidationExceptionRes(errorsAsMap, responseStatus, req).buildResponseEntity()
	}

	@ExceptionHandler(AuthenticationException::class)
	fun authenticationException(
		ex: AuthenticationException,
		req: HttpServletRequest
	): ResponseEntity<MessageExceptionRes> {
		log.error("Spring security context exception. Cause: {}", ex.message)
		return MessageExceptionRes(ex.message, HttpStatus.UNAUTHORIZED, req).buildResponseEntity()
	}

	@ExceptionHandler(Exception::class)
	fun unknowServerException(
		ex: Exception,
		req: HttpServletRequest
	): ResponseEntity<MessageExceptionRes> {
		log.error("Unexpected issue during server process. Cause: {}", ex.message)
		return MessageExceptionRes(
			message = i18nService.getMessage(ApiLocaleSet.EXC_UNKNOW_SERVER_EXCEPTION),
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
			req
		).buildResponseEntity()
	}
}
