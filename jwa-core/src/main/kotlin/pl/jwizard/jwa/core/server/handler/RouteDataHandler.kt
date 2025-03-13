package pl.jwizard.jwa.core.server.handler

import io.javalin.http.Context
import io.javalin.security.RouteRole
import pl.jwizard.jwl.server.route.RouteMethod
import pl.jwizard.jwl.server.route.handler.Handler

abstract class RouteDataHandler<T> : Handler() {
	protected abstract val callback: (Context, T) -> Unit

	protected abstract fun handleWithData(ctx: Context): T

	override val withRoles = emptyMap<RouteRole, List<RouteMethod>>()

	final override fun handle(ctx: Context) {
		callback(ctx, handleWithData(ctx))
	}
}
