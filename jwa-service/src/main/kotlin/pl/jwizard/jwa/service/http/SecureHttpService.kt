package pl.jwizard.jwa.service.http

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import okio.IOException
import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.http.HttpMethod
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.server.ApiHttpHeader
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.property.BaseEnvironment
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

@Component
internal class SecureHttpService(
	private val httpClient: HttpClient,
	private val objectMapper: ObjectMapper,
	environment: BaseEnvironment,
) {
	private val proxyVerifyToken = environment.getProperty<String>(AppBaseProperty.PROXY_VERIFY_TOKEN)

	fun prepareAndRunSecureHttpRequest(
		url: String,
		authToken: String? = null,
		authTokenType: AuthTokenType = AuthTokenType.PLAIN,
		httpMethod: HttpMethod = HttpMethod.GET,
		contentType: ApiContentType = ApiContentType.APPLICATION_JSON,
		body: String? = null,
		headers: Map<ApiHttpHeader, String> = emptyMap(),
		silent: Boolean = false,
		withProxyVerifyToken: Boolean = false,
	): JsonNode? {
		val httpRequest = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.method(
				httpMethod.asString(),
				body?.let { BodyPublishers.ofString(it) } ?: BodyPublishers.noBody(),
			)
			.header(HttpHeader.CONTENT_TYPE.asString(), contentType.mime)
			.apply {
				if (proxyVerifyToken.isNotBlank()) {
					header(ApiHttpHeader.X_CLOUDFLARE_VERIFY_PROXY.headerName, proxyVerifyToken)
				}
				authToken?.let { header(HttpHeader.AUTHORIZATION.asString(), authTokenType.type + it) }
				headers.forEach { header(it.key.headerName, it.value) }
			}
			.build()
		try {
			val response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
			return if (response.statusCode() == 200) {
				objectMapper.readTree(response.body())
			} else {
				null
			}
		} catch (ex: IOException) {
			if (silent) {
				return null
			}
			throw ex
		}
	}
}
