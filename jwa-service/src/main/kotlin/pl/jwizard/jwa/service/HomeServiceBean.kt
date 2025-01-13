/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.i18n.I18nServerDynamicSource
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.rest.route.home.dto.FeaturesResDto
import pl.jwizard.jwa.rest.route.home.dto.StatisticsInfoResDto
import pl.jwizard.jwa.rest.route.home.spi.HomeService
import pl.jwizard.jwa.service.spi.KeyFeaturesSupplier
import pl.jwizard.jwa.service.spi.ProjectPackagesSupplier
import pl.jwizard.jwl.command.Command
import pl.jwizard.jwl.command.Module
import pl.jwizard.jwl.i18n.I18nBean
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.radio.RadioStation

/**
 * Implementation of [HomeService] that provides data for the home page such as statistics and features.
 *
 * @property i18n The [I18nBean] used for fetching localized text values.
 * @property projectPackagesSupplier The [ProjectPackagesSupplier] used for retrieving project package count.
 * @property keyFeaturesSupplier The [KeyFeaturesSupplier] used for retrieving the application's key features.
 * @author Miłosz Gilga
 */
@SingletonService
class HomeServiceBean(
	private val i18n: I18nBean,
	private val projectPackagesSupplier: ProjectPackagesSupplier,
	private val keyFeaturesSupplier: KeyFeaturesSupplier,
	private val environment: EnvironmentBean,
) : HomeService {

	/**
	 * Retrieves statistics for the home page, including the number of modules, commands, radio stations, and open-source
	 * libraries.
	 *
	 * @return A [StatisticsInfoResDto] containing the statistics for the home page.
	 */
	override fun getHomePageStatistics() = StatisticsInfoResDto(
		modules = Module.entries.size,
		commands = Command.entries.size,
		radioStations = RadioStation.entries.size,
		openSourceLibraries = projectPackagesSupplier.getProjectPackagesCount()
	)

	/**
	 * Retrieves the list of key features for the home page, localized based on the provided language. The features are
	 * returned as a list of [FeaturesResDto], sorted by their active status in descending order.
	 *
	 * @param language The language code used for localization (optional).
	 * @return A list of [FeaturesResDto] containing the application's key features.
	 */
	override fun getHomePageFeatures(language: String?): List<FeaturesResDto> {
		val keyFeatures = keyFeaturesSupplier.getKeyFeatures()
		val args = mapOf("prefix" to environment.getProperty<String>(AppBaseProperty.GUILD_LEGACY_PREFIX))
		return keyFeatures
			.map { (textId, isActive) ->
				FeaturesResDto(
					name = i18n.tRaw(I18nServerDynamicSource.KEY_FEATURE_NAME, arrayOf(textId), language),
					description = i18n.tRaw(
						I18nServerDynamicSource.KEY_FEATURE_DESCRIPTION,
						arrayOf(textId),
						args,
						language
					),
					isActive,
				)
			}
			.sortedByDescending(FeaturesResDto::isActive)
	}
}
