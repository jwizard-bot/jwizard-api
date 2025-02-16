package pl.jwizard.jwa.rest.route.repository.spi

import pl.jwizard.jwa.rest.route.repository.dto.RepositoryResDto

interface RepositoryService {
	fun getAllRepositories(language: String?): List<RepositoryResDto>
}
