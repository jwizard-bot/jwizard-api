package pl.jwizard.jwa.gateway.http.rest.route.contributor

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.handler.I18nRouteHandler
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
internal class ContributorController(
	private val contributorService: ContributorService,
) : HttpControllerBase {
	override val basePath = "/v1/contributor"

	private val getAllProjectContributors = I18nRouteHandler { ctx, language ->
		val contributorsResDto = contributorService.getProjectContributors(language)
		ctx.json(contributorsResDto)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/all", getAllProjectContributors)
		.compositeRoutes()
}
