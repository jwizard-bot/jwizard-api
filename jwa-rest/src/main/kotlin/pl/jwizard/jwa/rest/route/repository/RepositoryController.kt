package pl.jwizard.jwa.rest.route.repository

import io.javalin.http.Context
import pl.jwizard.jwa.rest.route.repository.spi.RepositoryService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@SingletonController
class RepositoryController(private val repositoryService: RepositoryService) : RestControllerBase {
	override val basePath = "/v1/repository"

	private fun fetchAllRepositories(ctx: Context, language: String?) {
		val repositories = repositoryService.getAllRepositories(language)
		ctx.json(repositories)
	}

	override val routes = RouteDefinitionBuilder()
		.getWithI18n("/all", ::fetchAllRepositories)
		.compositeRoutes()
}
