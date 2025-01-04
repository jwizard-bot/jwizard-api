/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service.spi

import pl.jwizard.jwa.service.spi.dto.ProjectVersionRow

/**
 * Interface for supplying build information of projects.
 *
 * This interface defines the contract for retrieving build-related information of multiple projects, specifically
 * including details such as the project version and the last update timestamp.
 *
 * @author Miłosz Gilga
 */
interface BuildInfoSupplier {

	/**
	 * Fetches the build information for multiple projects.
	 *
	 * This method retrieves a list of project version information, including the version name, latest version number,
	 * and the last update timestamp for each project.
	 *
	 * @return A list of [ProjectVersionRow] containing the build information for each project.
	 */
	fun fetchProjectsBuildInfo(): List<ProjectVersionRow>
}
