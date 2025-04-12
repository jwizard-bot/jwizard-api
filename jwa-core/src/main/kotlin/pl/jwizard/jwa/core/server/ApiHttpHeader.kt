package pl.jwizard.jwa.core.server

import io.javalin.http.Context
import org.eclipse.jetty.http.HttpHeader
import pl.jwizard.jwl.http.AppHttpHeader

enum class ApiHttpHeader(override val headerName: String) : AppHttpHeader {
	USER_AGENT(HttpHeader.USER_AGENT.asString()),

	// custom
	X_CSRF_TOKEN("X-Csrf-Token"),
	CF_CONNECTING_IP("CF-Connecting-IP"),
	;

	companion object {
		fun Context.header(apiHttpHeader: ApiHttpHeader) = header(apiHttpHeader.headerName)
	}
}
