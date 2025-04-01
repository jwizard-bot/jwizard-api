package pl.jwizard.jwa.gateway.http.rest.route.analyzer

import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder
import pl.jwizard.jwl.server.route.handler.RouteHandler

@Component
internal class AnalyzerController(
	private val analyzerService: AnalyzerService,
) : HttpControllerBase {
	override val basePath = "/v1/analyzer"

	// return combined analyzer statistics from all repositories
	private val getCombinedAnalyzerStatistics = RouteHandler { ctx ->
		val resDto = analyzerService.getCombinedAnalyzerStatistics()
		ctx.json(resDto)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/combined", getCombinedAnalyzerStatistics)
		.compositeRoutes()
}
