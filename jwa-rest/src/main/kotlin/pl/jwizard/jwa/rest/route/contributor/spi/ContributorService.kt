package pl.jwizard.jwa.rest.route.contributor.spi

import pl.jwizard.jwa.rest.route.contributor.dto.ContributorsResDto

interface ContributorService {
	fun getProjectContributors(language: String?): ContributorsResDto
}
