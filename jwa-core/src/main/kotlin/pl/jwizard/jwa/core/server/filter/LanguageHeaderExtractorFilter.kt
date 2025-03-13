package pl.jwizard.jwa.core.server.filter

import io.javalin.http.Context
import org.eclipse.jetty.http.HttpHeader
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.ApiServerAttribute
import pl.jwizard.jwl.property.AppBaseListProperty
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.server.definedHeader
import pl.jwizard.jwl.server.filter.WebFilterBase
import pl.jwizard.jwl.server.setAttribute

@Component
class LanguageHeaderExtractorFilter(
	environment: BaseEnvironment,
) : WebFilterBase() {
	private val languages = environment.getListProperty<String>(AppBaseListProperty.I18N_LANGUAGES)
	private val defaultLanguage = environment
		.getProperty<String>(AppBaseProperty.I18N_DEFAULT_LANGUAGE)

	override fun filter(ctx: Context) {
		val langHeader = ctx.definedHeader(HttpHeader.ACCEPT_LANGUAGE)
		val language = languages.find { it == langHeader?.substring(0, 2) }
		ctx.setAttribute(ApiServerAttribute.I18N_LOCALE, language ?: defaultLanguage)
	}
}
