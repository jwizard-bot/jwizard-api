package pl.jwizard.jwa.http.rest.route.home

import pl.jwizard.jwa.http.rest.route.home.dto.FeaturesResDto
import pl.jwizard.jwa.http.rest.route.home.dto.StatisticsInfoResDto

interface HomeService {
	fun getHomePageStatistics(): StatisticsInfoResDto

	fun getHomePageFeatures(language: String?): List<FeaturesResDto>
}
