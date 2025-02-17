package pl.jwizard.jwa.rest.route.repository

import pl.jwizard.jwa.rest.route.repository.dto.RepositoryResDto

interface RepositoryService {
	fun getAllRepositories(language: String?): List<RepositoryResDto>
}
