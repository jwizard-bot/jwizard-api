package pl.jwizard.jwa.rest.route.contributor.dto

import java.io.Serializable

data class ProjectContributor(
	val nickname: String,
	val profileLink: String,
	val profileImageUrl: String,
	val variants: List<String>,
) : Serializable
