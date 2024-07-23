/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.features.home

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.jwizard.api.features.home.dto.KeyFeatureResDto
import pl.jwizard.api.features.home.dto.StatsInfoResDto

@RestController
@RequestMapping("/api/v1/home")
class HomeController(val homeService: HomeService) {
	@GetMapping("/stats")
	fun getHomePageStats(): ResponseEntity<StatsInfoResDto> = ResponseEntity.ok(homeService.getHomePageStats())

	@GetMapping("/key-features")
	fun getHomePageKeyFeatures(): ResponseEntity<List<KeyFeatureResDto>> =
		ResponseEntity.ok(homeService.getHomePageKeyFeaturs())
}
