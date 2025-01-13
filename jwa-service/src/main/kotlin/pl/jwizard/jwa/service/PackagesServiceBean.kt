/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacadeBean
import pl.jwizard.jwa.rest.route.packages.dto.PackageRowResDto
import pl.jwizard.jwa.rest.route.packages.spi.PackagesService
import pl.jwizard.jwa.service.spi.ProjectPackagesSupplier
import pl.jwizard.jwl.ioc.stereotype.SingletonService

/**
 * Service class responsible for handling package-related operations.
 *
 * @property cacheFacade The facade for accessing the cache.
 * @property projectPackagesSupplier The supplier interface for retrieving project packages.
 * @author Miłosz Gilga
 */
@SingletonService
class PackagesServiceBean(
	private val cacheFacade: CacheFacadeBean,
	private val projectPackagesSupplier: ProjectPackagesSupplier,
) : PackagesService {

	/**
	 * Fetches all packages from the cache or computes them if not present.
	 *
	 * This method tries to retrieve the list of project packages from the cache. If the list is not present, it computes
	 * the list using the [computeOnAbsent] method.
	 *
	 * @return A list of package data transfer objects (DTOs).
	 */
	override fun fetchAllPackages() = cacheFacade.getCachedList(CacheEntity.PROJECT_PACKAGES, 0, ::computeOnAbsent)

	/**
	 * Computes the list of all project packages.
	 *
	 * This method retrieves all project packages from the supplier and maps them to their respective DTOs.
	 *
	 * @return A list of package data transfer objects (DTOs).
	 */
	private fun computeOnAbsent() = projectPackagesSupplier
		.getAllProjectPackages()
		.map { PackageRowResDto(it.key, it.value) }
}
