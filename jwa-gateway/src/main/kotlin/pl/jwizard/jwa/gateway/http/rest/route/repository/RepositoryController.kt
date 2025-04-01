package pl.jwizard.jwa.gateway.http.rest.route.repository

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.handler.I18nRouteHandler
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
internal class RepositoryController(
	private val repositoryService: RepositoryService,
) : HttpControllerBase {
	override val basePath = "/v1/repository"

	private val fetchAllRepositories = I18nRouteHandler { ctx, language ->
		val repositories = repositoryService.getAllRepositories(language)
		ctx.json(repositories)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/all", fetchAllRepositories)
		.compositeRoutes()
}
