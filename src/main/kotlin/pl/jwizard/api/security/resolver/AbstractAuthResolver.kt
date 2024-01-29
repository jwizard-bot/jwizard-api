/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security.resolver

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.LocaleResolver
import pl.jwizard.api.exception.MessageExceptionRes
import pl.jwizard.api.i18n.I18nService
import pl.jwizard.api.i18n.set.ApiLocaleSet
import java.nio.charset.StandardCharsets

abstract class AbstractAuthResolver(
	private val i18nService: I18nService,
	private val localeResolver: LocaleResolver,
	private val objectMapper: ObjectMapper
) {
	protected fun chainRequest(
		req: HttpServletRequest?,
		res: HttpServletResponse?,
	) {
		if (req != null && res != null) {
			LocaleContextHolder.setLocale(localeResolver.resolveLocale(req))
			val status = HttpStatus.FORBIDDEN
			val resDto = MessageExceptionRes(
				message = i18nService.getMessage(ApiLocaleSet.EXC_AUTHENTICATION_EXCEPTION),
				status,
				req,
			)
			res.status = status.value()
			res.contentType = MediaType.APPLICATION_JSON_VALUE
			res.characterEncoding = StandardCharsets.UTF_8.name()
			res.writer.println(objectMapper.writeValueAsString(resDto))
		}
	}
}
