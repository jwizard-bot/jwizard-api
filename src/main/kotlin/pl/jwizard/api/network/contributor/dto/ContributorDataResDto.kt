/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.contributor.dto

import pl.jwizard.api.network.contributor.ContributeVariant
import java.io.Serializable

data class ContributorDataResDto(
	val nickname: String,
	val profileLink: String,
	val profileImageUrl: String,
	val variants: List<String>,
) : Serializable {
	constructor(requestData: ContributorData, variants: List<ContributeVariant>) : this(
		nickname = requestData.nickname,
		profileLink = requestData.profileLink,
		profileImageUrl = requestData.profileImageUrl,
		variants = ContributeVariant.getNamesFromVariants(variants),
	)
}
