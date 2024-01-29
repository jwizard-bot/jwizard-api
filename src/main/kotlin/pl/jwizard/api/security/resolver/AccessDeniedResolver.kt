/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security.resolver

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.servlet.LocaleResolver
import pl.jwizard.api.i18n.I18nService

@Component
class AccessDeniedResolver(
	i18nService: I18nService,
	localeResolver: LocaleResolver,
	objectMapper: ObjectMapper,
) : AbstractAuthResolver(i18nService, localeResolver, objectMapper), AccessDeniedHandler {

	override fun handle(
		req: HttpServletRequest?,
		res: HttpServletResponse?,
		ex: AccessDeniedException?
	) = chainRequest(req, res)
}
