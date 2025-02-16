package pl.jwizard.jwa.rest.route.resource

import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import io.javalin.http.queryParamAsClass
import pl.jwizard.jwa.core.server.ValidatorChainFacade
import pl.jwizard.jwa.rest.route.resource.spi.ResourceService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@SingletonController
class ResourceController(private val resourceService: ResourceService) : RestControllerBase {
	override val basePath = "/v1/asset"

	private fun fetchRawFile(ctx: Context) {
		val assetNameParam = ctx.queryParamAsClass<String>("name")
		val assetName = ValidatorChainFacade(assetNameParam).disallowBlanks().get()

		val resource = resourceService.getResource(assetName) ?: throw NotFoundResponse()

		val (contentTYpe, inputStream) = resource
		ctx.contentType(contentTYpe)
		ctx.result(inputStream)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/file", ::fetchRawFile)
		.compositeRoutes()
}
