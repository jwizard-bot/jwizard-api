package pl.jwizard.jwa.core.server

import io.javalin.http.Context
import org.eclipse.jetty.http.HttpHeader

enum class ApiHttpHeader(val headerName: String) {
	USER_AGENT(HttpHeader.USER_AGENT.asString()),

	// custom
	X_CSRF_TOKEN("X-Csrf-Token"),
	X_FORWARDED_FOR("X-Forwarded-For"),
	X_CLOUDFLARE_VERIFY_PROXY("X-Cloudflare-Verify-Proxy"),
	;

	companion object {
		fun Context.header(apiHttpHeader: ApiHttpHeader) = header(apiHttpHeader.headerName)
	}
}
