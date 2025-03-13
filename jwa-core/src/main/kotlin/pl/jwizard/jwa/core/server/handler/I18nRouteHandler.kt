package pl.jwizard.jwa.core.server.handler

import io.javalin.http.Context
import pl.jwizard.jwa.core.server.ApiServerAttribute
import pl.jwizard.jwl.server.getAttribute

class I18nRouteHandler(
	override val callback: (Context, String?) -> Unit,
) : RouteDataHandler<String?>() {
	override fun handleWithData(
		ctx: Context,
	) = ctx.getAttribute<String>(ApiServerAttribute.I18N_LOCALE)
}
