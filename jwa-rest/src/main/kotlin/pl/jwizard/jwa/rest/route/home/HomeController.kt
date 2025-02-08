/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.home

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.home.spi.HomeService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

/**
 * Controller for handling requests related to the home page, such as statistics and features.
 *
 * @property homeService The service used to retrieve home page statistics and feature details.
 * @author Miłosz Gilga
 */
@SingletonController
class HomeController(private val homeService: HomeService) : RestControllerBase {
	override val basePath = "/v1/home"

	/**
	 * Retrieves the statistics for the home page and returns it as a JSON response.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 */
	private fun getHomePageStatistics(ctx: Context) {
		val statistics = homeService.getHomePageStatistics()
		ctx.json(statistics)
	}

	/**
	 * Retrieves the features for the home page based on the user's locale and returns it as a JSON response.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 */
	private fun getHomePageFeatures(ctx: Context, language: String?) {
		val features = homeService.getHomePageFeatures(language)
		ctx.json(features)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/statistics", ::getHomePageStatistics)
		.getWithI18n("/features", ::getHomePageFeatures)
		.compositeRoutes()
}
