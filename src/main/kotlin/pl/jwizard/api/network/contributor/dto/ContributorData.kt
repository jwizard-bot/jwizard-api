/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.contributor.dto

import pl.jwizard.api.network.contributor.ContributeVariant

data class ContributorData(
	val nickname: String,
	val profileLink: String,
	val profileImageUrl: String,
	val variant: ContributeVariant,
)
