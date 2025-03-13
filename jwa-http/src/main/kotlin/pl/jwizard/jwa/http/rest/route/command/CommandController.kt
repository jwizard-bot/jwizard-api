package pl.jwizard.jwa.http.rest.route.command

import io.javalin.http.NotFoundResponse
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.handler.I18nRouteHandler
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder

@Component
internal class CommandController(private val commandService: CommandService) : HttpControllerBase {
	override val basePath = "/v1/command"

	private val getCommandModules = I18nRouteHandler { ctx, language ->
		val modules = commandService.getModules(language)
		ctx.json(modules)
	}

	private val getCommands = I18nRouteHandler { ctx, language ->
		val commands = commandService.getCommands(language)
		ctx.json(commands)
	}

	private val getCommandDetails = I18nRouteHandler { ctx, language ->
		val nameId = ctx.pathParam("nameId")
		val details = commandService.getCommandDetails(nameId, language) ?: throw NotFoundResponse()
		ctx.json(details)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/module/all", getCommandModules)
		.get("/all", getCommands)
		.get("/<nameId>/details", getCommandDetails)
		.compositeRoutes()
}
