/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.home

import io.javalin.http.Context
import pl.jwizard.jwa.rest.home.spi.HomeService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.attribute.CommonServerAttribute
import pl.jwizard.jwl.server.getAttribute
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinition

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
	 * @param ctx The Javalin [Context] used to handle the HTTP request and send the response.
	 */
	private fun getHomePageStatistics(ctx: Context) {
		val statistics = homeService.getHomePageStatistics()
		ctx.json(statistics)
	}

	/**
	 * Retrieves the features for the home page based on the user's locale and returns it as a JSON response.
	 *
	 * @param ctx The Javalin [Context] used to handle the HTTP request and send the response.
	 */
	private fun getHomePageFeatures(ctx: Context) {
		val language = ctx.getAttribute<String>(CommonServerAttribute.I18N_LOCALE)
		val features = homeService.getHomePageFeatures(language)
		ctx.json(features)
	}

	override val routes = RouteDefinition.Builder()
		.get("/statistics", ::getHomePageStatistics)
		.get("/features", ::getHomePageFeatures)
		.compositeRoutes()
}
