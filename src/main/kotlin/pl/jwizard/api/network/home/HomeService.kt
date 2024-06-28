/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.home

import pl.jwizard.api.network.home.dto.KeyFeatureResDto
import pl.jwizard.api.network.home.dto.StatsInfoResDto

interface HomeService {
	fun getHomePageStats(): StatsInfoResDto
	fun getHomePageKeyFeaturs(): List<KeyFeatureResDto>
}
