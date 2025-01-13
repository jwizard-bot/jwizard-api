/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.contributor.spi

import pl.jwizard.jwa.rest.route.contributor.dto.ContributorsResDto

/**
 * Service interface responsible for providing contributor data for a project.
 *
 * This service provides an abstract method to retrieve a list of contributors along with their associated details,
 * such as variants and profile information.
 *
 * @author Miłosz Gilga
 */
interface ContributorService {

	/**
	 * Retrieves a list of contributors for the project.
	 *
	 * This method returns a [ContributorsResDto] containing the contributor information along with the associated
	 * variants and optionally the initial variant.
	 *
	 * @param language The language code used to retrieve localized project names.
	 * @return A [ContributorsResDto] containing a list of contributors, their variants, and an optional initial variant.
	 */
	fun getProjectContributors(language: String?): ContributorsResDto
}
