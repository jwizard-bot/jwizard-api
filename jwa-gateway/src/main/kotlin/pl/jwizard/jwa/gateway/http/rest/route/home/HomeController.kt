package pl.jwizard.jwa.gateway.http.rest.route.home

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.handler.I18nRouteHandler
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder
import pl.jwizard.jwl.server.route.handler.RouteHandler

@Component
internal class HomeController(private val homeService: HomeService) : HttpControllerBase {
	override val basePath = "/v1/home"

	private val getHomePageStatistics = RouteHandler { ctx ->
		val statistics = homeService.getHomePageStatistics()
		ctx.json(statistics)
	}

	private val getHomePageFeatures = I18nRouteHandler { ctx, language ->
		val features = homeService.getHomePageFeatures(language)
		ctx.json(features)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/statistics", getHomePageStatistics)
		.get("/features", getHomePageFeatures)
		.compositeRoutes()
}
