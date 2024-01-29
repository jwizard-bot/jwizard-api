/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.i18n

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import pl.jwizard.api.scaffold.AbstractLoggingBean
import java.nio.charset.StandardCharsets
import java.util.*

@Configuration
class I18nConfiguration(
	private val _i18nProperties: I18nProperties
) {
	private final var defaultLocale: Locale = Locale.ENGLISH
	private final var availableLocales = listOf<Locale>()

	init {
		defaultLocale = Locale.forLanguageTag(_i18nProperties.defaultLocale)
		availableLocales = _i18nProperties.availableLocales.map { locale -> Locale.forLanguageTag(locale) }
	}

	@Primary
	@Bean("messageSource")
	fun messageSource(): MessageSource {
		val source = ResourceBundleMessageSource()
		source.addBasenames(*createLocaleBundlePaths())
		source.setDefaultEncoding(StandardCharsets.UTF_8.name())
		return source
	}

	@Bean("localeResolver")
	fun localeResolver(): LocaleResolver {
		val resolver = HeaderLocaleResolver(defaultLocale)
		resolver.setDefaultLocale(defaultLocale)
		resolver.supportedLocales = availableLocales
		return resolver
	}

	private fun createLocaleBundlePaths(): Array<String> {
		val basenames = arrayOfNulls<String>(_i18nProperties.localeBundles.size + 1)
		basenames[0] = "i18n/messages"
		_i18nProperties.localeBundles.toTypedArray().copyInto(basenames, destinationOffset = 1)
		_log.info("Successfully loaded messageSource bean context: {}", basenames)
		return basenames.requireNoNulls()
	}
}
