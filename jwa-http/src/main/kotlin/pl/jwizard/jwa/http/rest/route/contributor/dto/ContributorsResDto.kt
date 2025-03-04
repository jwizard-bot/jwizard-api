package pl.jwizard.jwa.http.rest.route.contributor.dto

data class ContributorsResDto(
	val contributors: List<ProjectContributor>,
	val variants: Map<String, ProjectVariant>,
	val initVariant: String,
)
