/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.features.contributor

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.jwizard.api.features.contributor.dto.ContributorsDataResDto

@RestController
@RequestMapping("/api/v1/contributor")
class ContributorController(val contributorService: ContributorService) {
	@GetMapping("/all")
	fun getAllContributorsWithVariants(): ResponseEntity<ContributorsDataResDto> =
		ResponseEntity.ok(contributorService.getAllContributorsWithVariants())
}
