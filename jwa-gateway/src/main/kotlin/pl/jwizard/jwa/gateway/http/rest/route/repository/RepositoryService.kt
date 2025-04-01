package pl.jwizard.jwa.gateway.http.rest.route.repository

import pl.jwizard.jwa.gateway.http.rest.route.repository.dto.RepositoryResDto

interface RepositoryService {
	fun getAllRepositories(language: String?): List<RepositoryResDto>
}
