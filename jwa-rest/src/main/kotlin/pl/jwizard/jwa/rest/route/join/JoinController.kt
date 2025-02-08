/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.join

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.join.spi.JoinService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

/**
 * Controller responsible for handling routes related to join functionality.
 *
 * This class exposes endpoints to fetch join instances and required permissions. It communicates with the [JoinService]
 * to retrieve the necessary data.
 *
 * @property joinService The service used to fetch join instances and required permissions.
 * @author Miłosz Gilga
 */
@SingletonController
class JoinController(private val joinService: JoinService) : RestControllerBase {
	override val basePath = "/v1/join"

	/**
	 * Handles the GET request for fetching all join instances. This method retrieves all available joinable instances
	 * using the [JoinService] and returns them as a JSON response.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 */
	private fun getAllInstances(ctx: Context) {
		val instances = joinService.fetchJoinInstances()
		ctx.json(instances)
	}

	/**
	 * Handles the GET request for fetching required permissions for joining.
	 *
	 * This method retrieves all permissions needed to join a specific instance and returns them as a JSON response.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 */
	private fun getRequiredPermissions(ctx: Context) {
		val instances = joinService.fetchRequiredPermissions()
		ctx.json(instances)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/instance/all", ::getAllInstances)
		.get("/permission/all", ::getRequiredPermissions)
		.compositeRoutes()
}
