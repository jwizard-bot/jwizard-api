package pl.jwizard.jwa.rest.route.contributor

import pl.jwizard.jwa.rest.route.contributor.dto.ContributorsResDto

interface ContributorService {
	fun getProjectContributors(language: String?): ContributorsResDto
}
