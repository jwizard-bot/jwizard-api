package pl.jwizard.jwa.core.server

import io.javalin.security.RouteRole

enum class Role : RouteRole {
	CSRF_PROTECTED,
	AUTHENTICATED,
	;
}
