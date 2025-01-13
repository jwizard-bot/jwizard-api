/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.home.spi

import pl.jwizard.jwa.rest.route.home.dto.FeaturesResDto
import pl.jwizard.jwa.rest.route.home.dto.StatisticsInfoResDto

/**
 * Service interface for retrieving home page related information.
 *
 * This interface provides methods to fetch statistical data and feature details for the home page, including the
 * number of modules, commands, radio stations, and open-source libraries, as well as active features based on the
 * provided language.
 *
 * @author Miłosz Gilga
 */
interface HomeService {

	/**
	 * Retrieves statistics for the home page.
	 *
	 * This method returns the general statistics such as the count of modules, commands, radio stations, and open-source
	 * libraries that are part of the system.
	 *
	 * @return An instance of [StatisticsInfoResDto] containing the statistical data.
	 */
	fun getHomePageStatistics(): StatisticsInfoResDto

	/**
	 * Retrieves the list of features available on the home page based on the specified language.
	 *
	 * This method returns a list of features, each with a name, description, and status of activity (active or inactive),
	 * based on the language provided.
	 *
	 * @param language The language in which the feature details should be returned (optional).
	 * @return A list of [FeaturesResDto] instances representing the available features.
	 */
	fun getHomePageFeatures(language: String?): List<FeaturesResDto>
}
