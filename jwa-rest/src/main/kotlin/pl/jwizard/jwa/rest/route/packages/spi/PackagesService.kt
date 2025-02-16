package pl.jwizard.jwa.rest.route.packages.spi

import pl.jwizard.jwa.rest.route.packages.dto.PackageRowResDto

interface PackagesService {
	fun fetchAllPackages(): List<PackageRowResDto>
}
