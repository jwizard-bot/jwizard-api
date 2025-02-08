/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.contributor

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.contributor.spi.ContributorService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

/**
 * Controller responsible for handling HTTP requests related to project contributors.
 *
 * This class defines the REST API endpoints for retrieving contributor data, and it communicates with the
 * [ContributorService] to fetch and return the data. It extends [RestControllerBase] to inherit common routing and
 * functionality.
 *
 * @property contributorService The service used to fetch project contributor data.
 * @author Miłosz Gilga
 */
@SingletonController
class ContributorController(private val contributorService: ContributorService) : RestControllerBase {
	override val basePath = "/v1/contributor"

	/**
	 * Handles the HTTP GET request to fetch all project contributors.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 */
	private fun getAllProjectContributors(ctx: Context, language: String?) {
		val contributorsResDto = contributorService.getProjectContributors(language)
		ctx.json(contributorsResDto)
	}

	override val routes = RouteDefinitionBuilder()
		.getWithI18n("/all", ::getAllProjectContributors)
		.compositeRoutes()
}
