/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.resource

import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import io.javalin.http.queryParamAsClass
import pl.jwizard.jwa.core.server.ValidatorChainFacade
import pl.jwizard.jwa.rest.route.resource.spi.ResourceService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

/**
 * Controller class responsible for handling HTTP requests related to static assets.
 *
 * @property resourceService Service for retrieving resources, responsible for locating and streaming asset files.
 * @author Miłosz Gilga
 */
@SingletonController
class ResourceController(private val resourceService: ResourceService) : RestControllerBase {
	override val basePath = "/v1/asset"

	/**
	 * Endpoint method that retrieves a specified asset by name and sends it as a binary response.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 */
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
