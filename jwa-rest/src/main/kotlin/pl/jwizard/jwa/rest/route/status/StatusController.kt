/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.status

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.status.spi.StatusService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

/**
 * Controller responsible for handling status-related REST API endpoints. Provides information about the status of the
 * application and its components.
 *
 * @property statusService Service responsible for retrieving status data.
 * @author Miłosz Gilga
 */
@SingletonController
class StatusController(private val statusService: StatusService) : RestControllerBase {
	override val basePath = "/v1/status"

	/**
	 * Handles the `/global` endpoint to fetch the global application status. This endpoint responds with localized
	 * information based on the requested language.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 */
	private fun getGlobalStatus(ctx: Context, language: String?) {
		val resDto = statusService.getGlobalStatus(language)
		ctx.json(resDto)
	}

	override val routes = RouteDefinitionBuilder()
		.getWithI18n("/global", ::getGlobalStatus)
		.compositeRoutes()
}
