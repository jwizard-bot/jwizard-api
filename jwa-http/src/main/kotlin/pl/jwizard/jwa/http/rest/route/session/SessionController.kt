package pl.jwizard.jwa.http.rest.route.session

import io.javalin.http.BadRequestResponse
import io.javalin.http.HttpStatus
import io.javalin.http.NotFoundResponse
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.cookie.ServerCookie
import pl.jwizard.jwa.core.server.cookie.ServerCookie.Companion.cookie
import pl.jwizard.jwa.core.server.cookie.ServerCookie.Companion.removeCookie
import pl.jwizard.jwa.core.server.handler.AuthRouteHandler
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder
import pl.jwizard.jwl.server.route.handler.RouteHandler

@Component
class SessionController(private val sessionService: SessionService) : HttpControllerBase {
	override val basePath = "/session"

	private val mySessions = AuthRouteHandler { ctx, loggedUser ->
		val resDto = sessionService.mySessions(loggedUser)
		ctx.json(resDto)
	}

	private val getCsrfToken = AuthRouteHandler { ctx, loggedUser ->
		val resDto = sessionService.updateAndGetCsrfToken(loggedUser.sessionId)
		ctx.json(resDto)
	}

	private val deleteMySessionBasedSessionId = AuthRouteHandler { ctx, loggedUser ->
		val sessionId = ctx.pathParam("sessionId")
		if (!sessionService.deleteMySessionBasedSessionId(sessionId, loggedUser)) {
			throw NotFoundResponse()
		}
		ctx.status(HttpStatus.NO_CONTENT)
	}

	private val deleteAllMySessions = AuthRouteHandler { ctx, loggedUser ->
		sessionService.deleteAllMySessionsWithoutCurrentSession(loggedUser)
		ctx.status(HttpStatus.NO_CONTENT)
	}

	private val geolocationProviderInfo = RouteHandler { ctx ->
		val resDto = sessionService.geolocationProviderInfo()
		ctx.json(resDto)
	}

	private val revalidate = RouteHandler { ctx ->
		// could be null, we must check, if session exists
		val sessionId = ctx.cookie(ServerCookie.SID)
		val resDto = sessionService.revalidate(sessionId)
		if (!resDto.loggedIn) {
			ctx.removeCookie(ServerCookie.SID)
		}
		ctx.json(resDto)
	}

	// do nothing, refreshed already in authentication filter
	private val update = AuthRouteHandler { ctx, _ ->
		ctx.status(HttpStatus.NO_CONTENT)
	}

	private val logout = AuthRouteHandler { ctx, loggedUser ->
		val isRevoked = sessionService.logout(loggedUser)
		if (!isRevoked) {
			// unable to revoke, probably session not persisted anymore
			throw BadRequestResponse()
		}
		ctx.removeCookie(ServerCookie.SID)
		ctx.status(HttpStatus.NO_CONTENT)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/@me/all", mySessions)
		.get("/@me/csrf", getCsrfToken)
		.delete("/@me/<sessionId>", deleteMySessionBasedSessionId)
		.delete("/@me/all", deleteAllMySessions)
		.get("/geolocation/info", geolocationProviderInfo)
		.get("/revalidate", revalidate)
		.patch("/update", update)
		.delete("/logout", logout)
		.compositeRoutes()
}
