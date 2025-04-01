package pl.jwizard.jwa.gateway.http.rest.route.packages

import pl.jwizard.jwa.gateway.http.rest.route.packages.dto.PackageRowResDto

interface PackagesService {
	fun fetchAllPackages(): List<PackageRowResDto>
}
