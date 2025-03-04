package pl.jwizard.jwa.http.rest.route.packages

import pl.jwizard.jwa.http.rest.route.packages.dto.PackageRowResDto

interface PackagesService {
	fun fetchAllPackages(): List<PackageRowResDto>
}
