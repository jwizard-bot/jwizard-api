package pl.jwizard.jwa.core.server.filter

import io.javalin.http.Context
import io.javalin.http.ForbiddenResponse
import io.javalin.http.UnauthorizedResponse
import io.javalin.security.RouteRole
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.ApiServerAttribute
import pl.jwizard.jwa.core.server.Role
import pl.jwizard.jwa.core.server.cookie.CustomHeader
import pl.jwizard.jwa.core.server.cookie.CustomHeader.Companion.header
import pl.jwizard.jwl.server.filter.RoleFilterBase
import pl.jwizard.jwl.server.getAttribute
import pl.jwizard.jwl.util.logger

@Component
class AntiCsrfProtectionFilter : RoleFilterBase() {
	companion object {
		private val log = logger<AntiCsrfProtectionFilter>()
	}

	override val roles = arrayOf<RouteRole>(Role.CSRF_PROTECTED)
	override val runIndex = 2

	override fun roleFilter(ctx: Context) {
		val session = ctx.getAttribute<LoggedUser>(ApiServerAttribute.AUTHENTICATED_USER)
			?: throw UnauthorizedResponse()
		if (session.csrfToken != ctx.header(CustomHeader.CSRF_TOKEN)) {
			log.debug("Passed csrf token is not equal to persisted csrf token.")
			throw ForbiddenResponse()
		}
	}
}
