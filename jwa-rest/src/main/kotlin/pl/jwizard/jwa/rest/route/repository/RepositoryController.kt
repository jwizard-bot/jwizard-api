/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.repository

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.repository.spi.RepositoryService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.attribute.CommonServerAttribute
import pl.jwizard.jwl.server.getAttribute
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinition

/**
 * Controller class responsible for handling HTTP requests related to repositories.
 *
 * This controller provides the API endpoints for fetching repository data. It integrates with the [RepositoryService]
 * to retrieve the list of repositories and their associated details, returning the data in JSON format.
 *
 * @property repositoryService The service used to fetch repository data.
 * @author Miłosz Gilga
 */
@SingletonController
class RepositoryController(private val repositoryService: RepositoryService) : RestControllerBase {
	override val basePath = "/v1/repository"

	/**
	 * Handles the request to fetch all repositories.
	 *
	 * This method retrieves the language from the request context and passes it to the [RepositoryService] to get the
	 * list of repositories. The list is then returned as a JSON response.
	 *
	 * @param ctx The Javalin context for the request, used to retrieve attributes and send the response.
	 */
	private fun fetchAllRepositories(ctx: Context) {
		val language = ctx.getAttribute<String>(CommonServerAttribute.I18N_LOCALE)
		val repositories = repositoryService.getAllRepositories(language)
		ctx.json(repositories)
	}

	override val routes = RouteDefinition.Builder()
		.get("/all", ::fetchAllRepositories)
		.compositeRoutes()
}