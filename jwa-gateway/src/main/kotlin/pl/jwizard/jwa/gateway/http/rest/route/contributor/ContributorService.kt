package pl.jwizard.jwa.gateway.http.rest.route.contributor

import pl.jwizard.jwa.gateway.http.rest.route.contributor.dto.ContributorsResDto

interface ContributorService {
	fun getProjectContributors(language: String?): ContributorsResDto
}
