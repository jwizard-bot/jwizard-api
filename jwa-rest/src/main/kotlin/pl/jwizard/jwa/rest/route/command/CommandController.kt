/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.command

import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import pl.jwizard.jwa.rest.route.command.spi.CommandService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

/**
 * REST controller responsible for handling command-related API requests. Provides endpoints to retrieve command
 * modules, command lists, and detailed command information.
 *
 * @property commandService The service responsible for handling command-related business logic.
 * @author Miłosz Gilga
 */
@SingletonController
class CommandController(private val commandService: CommandService) : RestControllerBase {
	override val basePath = "/v1/command"

	/**
	 * Retrieves all available command modules and returns them as a JSON response.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 */
	private fun getCommandModules(ctx: Context, language: String?) {
		val modules = commandService.getModules(language)
		ctx.json(modules)
	}

	/**
	 * Retrieves all commands grouped by their respective modules and returns them as a JSON response.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 */
	private fun getCommands(ctx: Context, language: String?) {
		val commands = commandService.getCommands(language)
		ctx.json(commands)
	}

	/**
	 * Retrieves detailed information about a specific command and returns it as a JSON response. If the command is not
	 * found, a [NotFoundResponse] (HTTP 404) is thrown.
	 *
	 * @param ctx The Javalin HTTP context, used for request handling and response manipulation.
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 */
	private fun getCommandDetails(ctx: Context, language: String?) {
		val nameId = ctx.pathParam("nameId")
		val details = commandService.getCommandDetails(nameId, language) ?: throw NotFoundResponse()
		ctx.json(details)
	}

	override val routes = RouteDefinitionBuilder()
		.getWithI18n("/module/all", ::getCommandModules)
		.getWithI18n("/all", ::getCommands)
		.getWithI18n("/<nameId>/details", ::getCommandDetails)
		.compositeRoutes()
}
