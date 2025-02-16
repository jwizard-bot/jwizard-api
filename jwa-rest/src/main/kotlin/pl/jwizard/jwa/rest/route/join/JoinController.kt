package pl.jwizard.jwa.rest.route.join

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.join.spi.JoinService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@SingletonController
class JoinController(private val joinService: JoinService) : RestControllerBase {
	override val basePath = "/v1/join"

	private fun getAllInstances(ctx: Context) {
		val instances = joinService.fetchJoinInstances()
		ctx.json(instances)
	}

	private fun getRequiredPermissions(ctx: Context) {
		val instances = joinService.fetchRequiredPermissions()
		ctx.json(instances)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/instance/all", ::getAllInstances)
		.get("/permission/all", ::getRequiredPermissions)
		.compositeRoutes()
}
