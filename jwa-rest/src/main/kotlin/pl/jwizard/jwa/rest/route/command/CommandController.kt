package pl.jwizard.jwa.rest.route.command

import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import pl.jwizard.jwa.rest.route.command.spi.CommandService
import pl.jwizard.jwl.ioc.stereotype.SingletonController
import pl.jwizard.jwl.server.route.RestControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@SingletonController
class CommandController(private val commandService: CommandService) : RestControllerBase {
	override val basePath = "/v1/command"

	private fun getCommandModules(ctx: Context, language: String?) {
		val modules = commandService.getModules(language)
		ctx.json(modules)
	}

	private fun getCommands(ctx: Context, language: String?) {
		val commands = commandService.getCommands(language)
		ctx.json(commands)
	}

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
