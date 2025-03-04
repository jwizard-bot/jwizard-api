package pl.jwizard.jwa.service

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacade
import pl.jwizard.jwa.http.rest.route.packages.PackagesService
import pl.jwizard.jwa.http.rest.route.packages.dto.PackageRowResDto
import pl.jwizard.jwa.service.spi.ProjectPackagesSupplier

@Component
internal class PackagesServiceImpl(
	private val cacheFacade: CacheFacade,
	private val projectPackagesSupplier: ProjectPackagesSupplier,
) : PackagesService {
	override fun fetchAllPackages() = cacheFacade.getCachedList(
		cacheKey = CacheEntity.PROJECT_PACKAGES, computeOnAbsent = {
			projectPackagesSupplier.getAllProjectPackages().map { PackageRowResDto(it.key, it.value) }
		})
}
