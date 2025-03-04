package pl.jwizard.jwa.http.rest.route.repository

import pl.jwizard.jwa.http.rest.route.repository.dto.RepositoryResDto

interface RepositoryService {
	fun getAllRepositories(language: String?): List<RepositoryResDto>
}
