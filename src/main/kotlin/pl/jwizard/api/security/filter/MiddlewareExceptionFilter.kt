/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.LocaleResolver

@Component
class MiddlewareExceptionFilter(
	private val handlerExceptionResolver: HandlerExceptionResolver,
	private val localeResolver: LocaleResolver,
) : AbstractFilterBean(MiddlewareExceptionFilter::class) {

	override fun doFilterInternal(
		req: HttpServletRequest,
		res: HttpServletResponse,
		chain: FilterChain
	) {
		try {
			LocaleContextHolder.setLocale(localeResolver.resolveLocale(req))
			chain.doFilter(req, res)
		} catch (ex: Exception) {
			log.error("Filter chain exception resolver executed exception. Details: {}", ex.message)
			handlerExceptionResolver.resolveException(req, res, null, ex)
		}
	}
}
