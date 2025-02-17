package pl.jwizard.jwa.rest.route.packages

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
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
