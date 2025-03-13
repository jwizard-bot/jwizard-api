package pl.jwizard.jwa.http.rest.route.join

import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder
import pl.jwizard.jwl.server.route.handler.RouteHandler

@Component
internal class JoinController(private val joinService: JoinService) : HttpControllerBase {
	override val basePath = "/v1/join"

	private val getAllInstances = RouteHandler { ctx ->
		val avatarSize = ctx.queryParam("avatarSize")
		val instances = joinService.fetchJoinInstances(avatarSize?.toIntOrNull())
		ctx.json(instances)
	}

	private val getRequiredPermissions = RouteHandler { ctx ->
		val instances = joinService.fetchRequiredPermissions()
		ctx.json(instances)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/instance/all", getAllInstances)
		.get("/permission/all", getRequiredPermissions)
		.compositeRoutes()
}
