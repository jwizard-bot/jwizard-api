/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.status

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.status.spi.StatusService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.attribute.CommonServerAttribute
import pl.jwizard.jwl.server.getAttribute
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinition

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
	 * @param ctx The Javalin context object that provides request and response handling.
	 */
	private fun getGlobalStatus(ctx: Context) {
		val language = ctx.getAttribute<String>(CommonServerAttribute.I18N_LOCALE)
		val resDto = statusService.getGlobalStatus(language)
		ctx.json(resDto)
	}

	override val routes = RouteDefinition.Builder()
		.get("/global", ::getGlobalStatus)
		.compositeRoutes()
}
