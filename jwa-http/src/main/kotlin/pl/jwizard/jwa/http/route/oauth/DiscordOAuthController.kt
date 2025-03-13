package pl.jwizard.jwa.http.route.oauth

import io.javalin.http.Context
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.cookie.ServerCookie
import pl.jwizard.jwa.core.server.cookie.ServerCookie.Companion.cookie
import pl.jwizard.jwa.core.util.ext.baseUrl
import pl.jwizard.jwl.server.route.HttpControllerBase
import pl.jwizard.jwl.server.route.RouteDefinitionBuilder
import pl.jwizard.jwl.server.route.handler.RouteHandler

@Component
internal class DiscordOAuthController(
	private val discordOAuthService: DiscordOAuthService,
) : HttpControllerBase {
	override val basePath = "/oauth"

	private val redirectToLoginUrl = RouteHandler { ctx ->
		val redirectUrl = getRedirectUrl(ctx)
		val loginUrl = discordOAuthService.generateLoginUrl(redirectUrl)
		ctx.redirect(loginUrl)
	}

	// create new cookie and add user to session only if authentication was succeeded
	private val authorizeAndRedirect = RouteHandler { ctx ->
		val code = ctx.queryParam("code")
		val redirectUrl = getRedirectUrl(ctx)
		val sidFromCookie = ctx.cookie(ServerCookie.SID)
		val res = discordOAuthService.authorize(
			code, redirectUrl, sidFromCookie, ctx.ip(), ctx.userAgent()
		)
		if (res.sessionId != null) {
			val sidCookie = ServerCookie.SID.toCookieInstance(
				value = res.sessionId,
				ttl = res.sessionTtl,
				httpOnly = true,
				secure = true,
			)
			ctx.cookie(sidCookie)
		}
		ctx.redirect(res.redirectUrl)
	}

	private fun getRedirectUrl(ctx: Context): String? {
		val baseUrl = ctx.baseUrl()
		return if (baseUrl == null) null else "$baseUrl$basePath/discord/redirect"
	}

	override val routes = RouteDefinitionBuilder()
		.get("/discord/login", redirectToLoginUrl)
		.get("/discord/redirect", authorizeAndRedirect)
		.compositeRoutes()
}
