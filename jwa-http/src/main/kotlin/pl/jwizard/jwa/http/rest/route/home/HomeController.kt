package pl.jwizard.jwa.http.rest.route.home

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
internal class HomeController(private val homeService: HomeService) : HttpControllerBase {
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
