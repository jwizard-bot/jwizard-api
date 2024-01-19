/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.i18n

import java.util.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver

class HeaderLocaleResolver(
	private val _defaultLocale: Locale
) : AcceptHeaderLocaleResolver() {
	override fun resolveLocale(req: HttpServletRequest): Locale {
		val acceptLanguage = req.getHeader(HttpHeaders.ACCEPT_LANGUAGE)
		if (acceptLanguage.isNullOrEmpty()) {
			return _defaultLocale
		}
		val languageRanges = Locale.LanguageRange.parse(acceptLanguage)
		val currentLocale = Locale.lookup(languageRanges, supportedLocales)
		LocaleContextHolder.setLocale(currentLocale)
		return currentLocale
	}
}
