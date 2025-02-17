package pl.jwizard.jwa.rest.route.contributor

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
class ContributorController(
	private val contributorService: ContributorService,
) : RestControllerBase {
	override val basePath = "/v1/contributor"

	private fun getAllProjectContributors(ctx: Context, language: String?) {
		val contributorsResDto = contributorService.getProjectContributors(language)
		ctx.json(contributorsResDto)
	}

	override val routes = RouteDefinitionBuilder()
		.getWithI18n("/all", ::getAllProjectContributors)
		.compositeRoutes()
}
