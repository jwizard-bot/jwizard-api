/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service.spi

/**
 * Interface for supplying the project packages information from various resource suppliers.
 *
 * @author Miłosz Gilga
 */
interface ProjectPackagesSupplier {

	/**
	 * Retrieves the total count of packages in the project.
	 *
	 * This method returns the number of project packages, which could include open-source libraries, dependencies,
	 * or any other packages used within the project.
	 *
	 * @return The total count of project packages as an [Int].
	 */
	fun getProjectPackagesCount(): Int

	/**
	 * Retrieves all project packages with their respective versions.
	 *
	 * This method returns a map containing the names of all project packages along with their versions, as specified in
	 * the project's dependencies.
	 *
	 * @return A [Map] where the key is the package name and the value is the package version.
	 */
	fun getAllProjectPackages(): Map<String, String>
}
