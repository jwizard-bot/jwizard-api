/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.packages

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.packages.spi.PackagesService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

/**
 * Controller for handling package-related HTTP requests.
 *
 * This controller defines the endpoints for fetching package data, utilizing the [PackagesService] to retrieve and
 * provide the necessary package information.
 *
 * @property packagesService The service used to fetch package data.
 * @author Miłosz Gilga
 */
@SingletonController
class PackagesController(private val packagesService: PackagesService) : RestControllerBase {
	override val basePath = "/v1/packages"

	/**
	 * Fetches all packages and responds with the package data in JSON format. This method retrieves all packages using
	 * the PackagesService and sends the data as a JSON response.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 */
	private fun fetchAllPackages(ctx: Context) {
		val packages = packagesService.fetchAllPackages()
		ctx.json(packages)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/all", ::fetchAllPackages)
		.compositeRoutes()
}
