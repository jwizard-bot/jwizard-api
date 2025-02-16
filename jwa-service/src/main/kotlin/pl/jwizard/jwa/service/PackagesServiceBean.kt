package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacadeBean
import pl.jwizard.jwa.rest.route.packages.dto.PackageRowResDto
import pl.jwizard.jwa.rest.route.packages.spi.PackagesService
import pl.jwizard.jwa.service.spi.ProjectPackagesSupplier
import pl.jwizard.jwl.ioc.stereotype.SingletonService

@SingletonService
class PackagesServiceBean(
	private val cacheFacade: CacheFacadeBean,
	private val projectPackagesSupplier: ProjectPackagesSupplier,
) : PackagesService {
	override fun fetchAllPackages() = cacheFacade
		.getCachedList(CacheEntity.PROJECT_PACKAGES, 0, ::computeOnAbsent)

	private fun computeOnAbsent() = projectPackagesSupplier
		.getAllProjectPackages()
		.map { PackageRowResDto(it.key, it.value) }
}
