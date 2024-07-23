/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.features.home

import pl.jwizard.api.features.home.dto.KeyFeatureResDto
import pl.jwizard.api.features.home.dto.StatsInfoResDto

interface HomeService {
	fun getHomePageStats(): StatsInfoResDto
	fun getHomePageKeyFeaturs(): List<KeyFeatureResDto>
}
