/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import org.jsoup.Jsoup
import pl.jwizard.jwa.core.i18n.I18nUtilLocaleSource
import pl.jwizard.jwa.rest.route.status.dto.GlobalStatusResDto
import pl.jwizard.jwa.rest.route.status.spi.StatusService
import pl.jwizard.jwl.i18n.I18nBean
import pl.jwizard.jwl.ioc.stereotype.SingletonService

/**
 * Service bean responsible for retrieving the global operational status of the JWizard system. It scrapes the status
 * page and maps the information to a localized response.
 *
 * @property i18n A bean responsible for providing localized translations for the status description.
 * @author Miłosz Gilga
 */
@SingletonService
class StatusServiceBean(private val i18n: I18nBean) : StatusService {

	companion object {
		/**
		 * The base URL of the JWizard status page.
		 */
		private const val STATUS_PAGE_URL = "https://status.jwizard.pl"
	}

	/**
	 * Retrieves the global status of the system by scraping the status page.
	 *
	 * It determines the operational state based on the CSS classes in the badge's SVG element and provides a localized
	 * description of the status. If SVG element could not be found, sets operational status to `null`.
	 *
	 * @param language The language code for localization (ex. "en", "pl").
	 * @return A [GlobalStatusResDto] object containing the operational state, a localized description, and the source
	 *         URL of the status page.
	 */
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
