package pl.jwizard.jwa.rest.route.home

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.home.spi.HomeService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@SingletonController
class HomeController(private val homeService: HomeService) : RestControllerBase {
	override val basePath = "/v1/home"

	private fun getHomePageStatistics(ctx: Context) {
		val statistics = homeService.getHomePageStatistics()
		ctx.json(statistics)
	}

	private fun getHomePageFeatures(ctx: Context, language: String?) {
		val features = homeService.getHomePageFeatures(language)
		ctx.json(features)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/statistics", ::getHomePageStatistics)
		.getWithI18n("/features", ::getHomePageFeatures)
		.compositeRoutes()
}
