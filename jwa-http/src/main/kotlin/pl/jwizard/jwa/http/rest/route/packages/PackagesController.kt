package pl.jwizard.jwa.http.rest.route.packages

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
internal class PackagesController(
	private val packagesService: PackagesService,
) : HttpControllerBase {
	override val basePath = "/v1/packages"

	private fun fetchAllPackages(ctx: Context) {
		val packages = packagesService.fetchAllPackages()
		ctx.json(packages)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/all", ::fetchAllPackages)
		.compositeRoutes()
}
