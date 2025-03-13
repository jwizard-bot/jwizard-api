package pl.jwizard.jwa.http.rest.route.actuator

import org.springframework.stereotype.Component
import pl.jwizard.jwa.http.rest.route.actuator.dto.ActuatorHealthResDto
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder
import pl.jwizard.jwl.server.route.handler.RouteHandler

@Component
internal class ActuatorController : HttpControllerBase {
	override val basePath = "/actuator"

	private val getHealth = RouteHandler { ctx ->
		val resDto = ActuatorHealthResDto(status = "UP")
		ctx.json(resDto)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/health", getHealth)
		.compositeRoutes()
}
