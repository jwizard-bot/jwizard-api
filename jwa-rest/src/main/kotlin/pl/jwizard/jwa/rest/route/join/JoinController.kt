package pl.jwizard.jwa.rest.route.join

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
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
