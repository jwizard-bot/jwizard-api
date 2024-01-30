/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.i18n

import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service

@Service
class I18nService(
	private val i18nConfiguration: I18nConfiguration
) {
	fun getMessage(placeholder: String?, params: Map<String, Any>): String {
		val messageSource = i18nConfiguration.messageSource()
		var text: String
		try {
			if (placeholder == null) {
				return ""
			}
			text = messageSource.getMessage(placeholder, null, LocaleContextHolder.getLocale())
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

	fun getMessage(placeholder: String): String = getMessage(placeholder, emptyMap())

	fun getMessage(placeholder: LocaleSet): String = getMessage(placeholder.getPlaceholder(), emptyMap())

	fun getMessage(
		placeholder: LocaleSet,
		variables: Map<String, Any>
	): String = getMessage(placeholder.getPlaceholder(), variables)
}
