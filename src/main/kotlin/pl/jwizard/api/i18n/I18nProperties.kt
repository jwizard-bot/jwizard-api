/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.i18n

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwizard.i18n")
data class I18nProperties(
	var defaultLocale: String = "en-US",
	var availableLocales: List<String> = emptyList(),
	var localeBundles: List<String> = emptyList(),
)
