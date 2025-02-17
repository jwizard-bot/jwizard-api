package pl.jwizard.jwa.service

import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.i18n.I18nUtilLocaleSource
import pl.jwizard.jwa.rest.route.status.StatusService
import pl.jwizard.jwa.rest.route.status.dto.GlobalStatusResDto
import pl.jwizard.jwl.i18n.I18n

@Component
internal class StatusServiceImpl(private val i18n: I18n) : StatusService {
	companion object {
		private const val STATUS_PAGE_URL = "https://status.jwizard.pl"
	}

	override fun getGlobalStatus(language: String?): GlobalStatusResDto {
		val document = Jsoup.connect("$STATUS_PAGE_URL/badge").get()
		val svgIcon = document.selectFirst("svg#root")

		var i18nDescriptionKey = I18nUtilLocaleSource.STATUS_UNKNOWN
		var operational: Boolean? = null

		if (svgIcon != null) {
			val classNames = svgIcon.classNames()
			operational = when {
				classNames.contains("text-statuspage-green") -> true
				classNames.contains("text-statuspage-red") -> false
				else -> null
			}
			if (operational != null) {
				i18nDescriptionKey = if (operational) {
					I18nUtilLocaleSource.STATUS_OPERATIONAL
				} else {
					I18nUtilLocaleSource.STATUS_DOWN
				}
			}
		}
		return GlobalStatusResDto(
			operational,
			description = i18n.t(i18nDescriptionKey, language),
			sourceUrl = STATUS_PAGE_URL
		)
	}
}
