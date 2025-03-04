package pl.jwizard.jwa.http.rest.route.contributor

import pl.jwizard.jwa.http.rest.route.contributor.dto.ContributorsResDto

interface ContributorService {
	fun getProjectContributors(language: String?): ContributorsResDto
}
