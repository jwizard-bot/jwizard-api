package pl.jwizard.jwa.http.route.oauth

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.CustomHeader
import pl.jwizard.jwa.core.server.CustomHeader.Companion.header
import pl.jwizard.jwa.core.server.ServerCookie
import pl.jwizard.jwa.core.server.ServerCookie.Companion.cookie
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder
import pl.jwizard.jwl.server.route.handler.RouteHandler

@Component
internal class DiscordOAuthController(
	private val discordOAuthService: DiscordOAuthService,
) : HttpControllerBase {
	override val basePath = "/oauth"

	private val redirectToLoginUrl = RouteHandler { ctx ->
		val loginUrl = discordOAuthService.generateLoginUrl(basePath)
		ctx.redirect(loginUrl)
	}

	// create new cookie and add user to session only if authentication was succeeded
	private val authorizeAndRedirect = RouteHandler { ctx ->
		val code = ctx.queryParam("code")
		val sidFromCookie = ctx.cookie(ServerCookie.SID)
		val ipAddress = ctx.header(CustomHeader.FORWARDED_FOR)
		// get first proxy ip address (host address), every proxy server added own ip address
		// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Forwarded-For
		val firstProxyIp = ipAddress?.split(",")?.firstOrNull()?.trim()
		val res = discordOAuthService.authorize(
			code, basePath, sidFromCookie, firstProxyIp, ctx.userAgent()
		)
		if (res.sessionId != null) {
			val sidCookie = ServerCookie.SID.toCookieInstance(
				value = res.sessionId,
				ttl = res.sessionTtl,
				domain = res.domain,
				httpOnly = true,
				secure = true,
			)
			ctx.cookie(sidCookie)
		}
		ctx.redirect(res.redirectUrl)
	}

	override val routes = RouteDefinitionBuilder()
		.get("/discord/login", redirectToLoginUrl)
		.get("/discord/redirect", authorizeAndRedirect)
		.compositeRoutes()
}
