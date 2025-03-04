package pl.jwizard.jwa.http.rest.route.analyzer

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
internal class AnalyzerController(
	private val analyzerService: AnalyzerService,
) : HttpControllerBase {
	override val basePath = "/v1/analyzer"

	// return combined analyzer statistics from all repositories
	private fun getCombinedAnalyzerStatistics(ctx: Context) {
		val resDto = analyzerService.getCombinedAnalyzerStatistics()
		ctx.json(resDto)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/combined", ::getCombinedAnalyzerStatistics)
		.compositeRoutes()
}
