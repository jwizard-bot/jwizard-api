package pl.jwizard.jwa.http.rest.route.repository

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
internal class RepositoryController(
	private val repositoryService: RepositoryService,
) : HttpControllerBase {
	override val basePath = "/v1/repository"

	private fun fetchAllRepositories(ctx: Context, language: String?) {
		val repositories = repositoryService.getAllRepositories(language)
		ctx.json(repositories)
	}

	override val routes = RouteDefinitionBuilder()
		.getWithI18n("/all", ::fetchAllRepositories)
		.compositeRoutes()
}
