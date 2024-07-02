/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.i18n

import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class I18nService(
	private val i18nConfiguration: I18nConfiguration
) {
	fun getMessage(placeholder: String?, params: Map<String, Any>, locale: Locale): String {
		val messageSource = i18nConfiguration.messageSource()
		var text: String
		try {
			if (placeholder == null) {
				return ""
			}
			text = messageSource.getMessage(placeholder, null, locale)
			if (text.isBlank()) {
				return text
			}
			for ((key, value) in params) {
				text = text.replace("{{${key}}}", value.toString())
			}
			return text
		} catch (ex: NoSuchMessageException) {
			return placeholder ?: ""
		}
	}

	fun getMessage(placeholder: String): String = getMessage(placeholder, emptyMap(), LocaleContextHolder.getLocale())

	fun getMessage(placeholder: LocaleSet): String =
		getMessage(placeholder.getPlaceholder(), emptyMap(), LocaleContextHolder.getLocale())

	fun getMessage(
		placeholder: LocaleSet,
		variables: Map<String, Any>
	): String = getMessage(placeholder.getPlaceholder(), variables, LocaleContextHolder.getLocale())

	fun getMessage(placeholder: String, locale: Locale): String = getMessage(placeholder, emptyMap(), locale)

	fun getCurrentLanguage(): String = LocaleContextHolder.getLocale().toLanguageTag()
}
