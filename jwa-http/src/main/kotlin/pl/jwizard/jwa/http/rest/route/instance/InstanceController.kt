package pl.jwizard.jwa.http.rest.route.instance

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.handler.I18nRouteHandler
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder
import pl.jwizard.jwl.server.route.handler.RouteHandler

@Component
internal class InstanceController(
	private val instanceService: InstanceService,
) : HttpControllerBase {
	override val basePath = "/v1/instance"

	private val getAllInstanceOptions = I18nRouteHandler { ctx, language ->
		val resDto = instanceService.getAllInstanceOptions(language)
		ctx.json(resDto)
	}

	private val getAllInstanceDefinitions = RouteHandler { ctx ->
		val avatarSize = ctx.queryParam("avatarSize")
		val resDto = instanceService.getAllInstanceDefinitions(avatarSize?.toIntOrNull())
		ctx.json(resDto)
	}

	private val getAllInstanceIds = RouteHandler { ctx ->
		val resDto = instanceService.getAllInstanceIds()
		ctx.json(resDto)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/option/all", getAllInstanceOptions)
		.get("/definition/all", getAllInstanceDefinitions)
		.get("/id/all", getAllInstanceIds)
		.compositeRoutes()
}
