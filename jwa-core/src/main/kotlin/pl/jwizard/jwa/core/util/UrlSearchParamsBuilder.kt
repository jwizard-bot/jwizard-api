package pl.jwizard.jwa.core.util

import java.net.URLEncoder

class UrlSearchParamsBuilder {
	private var baseUrl: String? = null
	private val params = mutableMapOf<String, Any>()

	fun setBaseUrl(baseUrl: String) = apply {
		this.baseUrl = baseUrl
	}

	fun addParam(key: String, value: Any) = apply {
		params += URLEncoder.encode(key, Charsets.UTF_8) to URLEncoder.encode(
			value.toString(),
			Charsets.UTF_8,
		)
	}

	fun build(): String {
		requireNotNull(baseUrl) { "Base URL must not be null." }
		var i = 0
		val builder = StringBuilder(baseUrl)
		for ((key, value) in params) {
			builder.append(if (i++ > 0) "&" else "?")
			builder.append(key)
			builder.append("=")
			builder.append(value)
		}
		return builder.toString()
	}

	fun buildAsFormData() = params.map { "${it.key}=${it.value}" }.joinToString("&")
}
