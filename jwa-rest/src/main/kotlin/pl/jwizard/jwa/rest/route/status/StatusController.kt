package pl.jwizard.jwa.rest.route.status

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
class StatusController(private val statusService: StatusService) : RestControllerBase {
	override val basePath = "/v1/status"

	private fun getGlobalStatus(ctx: Context, language: String?) {
		val resDto = statusService.getGlobalStatus(language)
		ctx.json(resDto)
	}

	override val routes = RouteDefinitionBuilder()
		.getWithI18n("/global", ::getGlobalStatus)
		.compositeRoutes()
}
