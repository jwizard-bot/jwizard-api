package pl.jwizard.jwa.rest.route.packages

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.packages.spi.PackagesService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@SingletonController
class PackagesController(private val packagesService: PackagesService) : RestControllerBase {
	override val basePath = "/v1/packages"

	private fun fetchAllPackages(ctx: Context) {
		val packages = packagesService.fetchAllPackages()
		ctx.json(packages)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/all", ::fetchAllPackages)
		.compositeRoutes()
}
