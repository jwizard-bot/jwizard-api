/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.packages.spi

import pl.jwizard.jwa.rest.route.packages.dto.PackageRowResDto

/**
 * Service interface for handling package-related operations.
 *
 * This interface provides methods to interact with package data, allowing for fetching all project packages.
 *
 * @author Miłosz Gilga
 */
interface PackagesService {

	/**
	 * Fetches all packages in the project.
	 *
	 * This method retrieves a list of all package data transfer objects (DTOs) representing the packages used in the
	 * project.
	 *
	 * @return A list of PackageRowResDto objects containing package information.
	 */
	fun fetchAllPackages(): List<PackageRowResDto>
}
