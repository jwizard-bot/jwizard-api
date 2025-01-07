/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.documentation

import io.javalin.http.Context
import pl.jwizard.jwa.rest.documentation.spi.DocumentationService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.attribute.CommonServerAttribute
import pl.jwizard.jwl.server.getAttribute
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinition

/**
 * REST controller for handling operations related to documentation resources.
 *
 * This controller provides endpoints to retrieve documentation data through a RESTful API. It uses
 * [DocumentationService] to fetch the documentation resources and responds to HTTP requests with appropriate data in
 * JSON format.
 *
 * @property documentationService The service responsible for fetching documentation resources.
 * @author Miłosz Gilga
 */
@SingletonController
class DocumentationController(private val documentationService: DocumentationService) : RestControllerBase {
	override val basePath = "/v1/documentation"

	/**
	 * Handles the `GET /v1/documentation/all` endpoint.
	 *
	 * Retrieves all available documentation resources, optionally filtered by language, and sends them as a JSON
	 * response.
	 *
	 * @param ctx The [Context] object representing the HTTP request and response context.
	 */
	private fun getAllDocumentations(ctx: Context) {
		val language = ctx.getAttribute<String>(CommonServerAttribute.I18N_LOCALE)
		val documentations = documentationService.getAllDocumentations(language)
		ctx.json(documentations)
	}

	override val routes = RouteDefinition.Builder()
		.get("/all", ::getAllDocumentations)
		.compositeRoutes()
}
