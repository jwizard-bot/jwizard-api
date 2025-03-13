package pl.jwizard.jwa.core.util.ext

import io.javalin.http.Context

// get base url in format: scheme://host:[port, only if not 80 or 443]
// if host is not defined, return null
fun Context.baseUrl(): String? {
	val scheme = this.scheme()
	val host = this.host() ?: return null

	val hostWithOptionalPort = if ((scheme == "http" && host.endsWith(":80")) ||
		(scheme == "https" && host.endsWith(":443"))
	) {
		host.substring(0, host.lastIndexOf(':'))
	} else {
		host
	}
	return "$scheme://$hostWithOptionalPort"
}
