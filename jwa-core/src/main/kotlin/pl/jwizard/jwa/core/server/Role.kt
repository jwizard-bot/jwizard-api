package pl.jwizard.jwa.core.server

import pl.jwizard.jwl.server.filter.FilterRole

enum class Role : FilterRole {
	CSRF_PROTECTED,
	AUTHENTICATED,
	;

	override val id
		get() = name
}
