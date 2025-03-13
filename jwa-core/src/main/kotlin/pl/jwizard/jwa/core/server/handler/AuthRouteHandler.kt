package pl.jwizard.jwa.core.server.handler

import io.javalin.http.Context
import pl.jwizard.jwa.core.server.ApiServerAttribute
import pl.jwizard.jwa.core.server.Role
import pl.jwizard.jwa.core.server.filter.LoggedUser
import pl.jwizard.jwl.server.getAttribute
import pl.jwizard.jwl.server.route.RouteMethod

class AuthRouteHandler(
	override val callback: (Context, LoggedUser) -> Unit,
) : RouteDataHandler<LoggedUser>() {
	override val withRoles = mapOf(
		forAllRouteMethods(Role.AUTHENTICATED),
		forRouteMethods(
			Role.CSRF_PROTECTED,
			RouteMethod.PUT,
			RouteMethod.POST,
			RouteMethod.PATCH,
			RouteMethod.DELETE,
		)
	)

	override fun handleWithData(
		ctx: Context,
	) = ctx.getAttribute<LoggedUser>(ApiServerAttribute.AUTHENTICATED_USER)!!
}
