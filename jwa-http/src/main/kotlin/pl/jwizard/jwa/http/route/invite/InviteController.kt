package pl.jwizard.jwa.http.route.invite

import io.javalin.http.NotFoundResponse
import io.javalin.http.pathParamAsClass
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.ValidatorChainFacade
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder
import pl.jwizard.jwl.server.route.handler.RouteHandler

@Component
class InviteController(private val inviteService: InviteService) : HttpControllerBase {
	override val basePath = "/invite"

	private val redirectToJoinBotInstance = RouteHandler { ctx ->
		val instanceId = ctx.pathParamAsClass<Int>("instanceId")
		val parsedInstanceId = ValidatorChainFacade(instanceId).get()
		val redirectUrl = inviteService.createInviteRedirectUrl(parsedInstanceId)
			?: throw NotFoundResponse()
		ctx.redirect(redirectUrl)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/bot/<instanceId>", redirectToJoinBotInstance)
		.compositeRoutes()
}
