/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.features.contributor

import pl.jwizard.api.features.contributor.dto.ContributorsDataResDto

interface ContributorService {
	fun getAllContributorsWithVariants(): ContributorsDataResDto
}
