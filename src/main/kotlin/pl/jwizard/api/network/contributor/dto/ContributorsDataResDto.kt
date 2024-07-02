/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.contributor.dto

data class ContributorsDataResDto(
	val contributors: List<ContributorDataResDto>,
	val allVariants: List<String>,
	val initVariant: String,
)
