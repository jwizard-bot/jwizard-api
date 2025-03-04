package pl.jwizard.jwa.http.rest.route.instance

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
internal class InstanceController(
	private val instanceService: InstanceService,
) : RestControllerBase {
	override val basePath = "/v1/instance"

	private fun getAllInstanceOptions(ctx: Context, language: String?) {
		val resDto = instanceService.getAllInstanceOptions(language)
		ctx.json(resDto)
	}

	private fun getAllInstanceDefinitions(ctx: Context) {
		val avatarSize = ctx.queryParam("avatarSize")
		val resDto = instanceService.getAllInstanceDefinitions(avatarSize?.toIntOrNull())
		ctx.json(resDto)
	}

	private fun getAllInstanceIds(ctx: Context) {
		val resDto = instanceService.getAllInstanceIds()
		ctx.json(resDto)
	}

	override val routes = RouteDefinitionBuilder()
		.getWithI18n("/option/all", ::getAllInstanceOptions)
		.get("/definition/all", ::getAllInstanceDefinitions)
		.get("/id/all", ::getAllInstanceIds)
		.compositeRoutes()
}
