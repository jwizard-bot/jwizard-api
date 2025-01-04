/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.repository.spi

import pl.jwizard.jwa.rest.repository.dto.RepositoryResDto

/**
 * Service interface responsible for managing repository data.
 *
 * This service provides methods for retrieving information about repositories, such as their name, description,
 * primary language, last update, and license.
 *
 * @author Miłosz Gilga
 */
interface RepositoryService {

	/**
	 * Retrieves all repositories with the associated metadata.
	 *
	 * This method fetches the list of repositories and their associated data, including the name, description, primary
	 * programming language, last update details, and license. The results can be localized based on the provided
	 * language parameter.
	 *
	 * @param language The language code for localization. If `null`, the default language will be used.
	 * @return A list of [RepositoryResDto] objects representing the repositories.
	 */
	fun getAllRepositories(language: String?): List<RepositoryResDto>
}
