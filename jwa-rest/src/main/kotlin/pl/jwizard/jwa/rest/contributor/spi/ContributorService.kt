/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.contributor.spi

import pl.jwizard.jwa.rest.contributor.dto.ContributorsResDto

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
	 * @return A [ContributorsResDto] containing a list of contributors, their variants, and an optional initial variant.
	 */
	fun getProjectContributors(): ContributorsResDto
}