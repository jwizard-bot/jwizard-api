package pl.jwizard.jwa.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.javalin.http.ContentType
import okio.IOException
import org.eclipse.jetty.http.HttpHeader
import org.eclipse.jetty.http.HttpMethod
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse

@Component
class SecureHttpService(
	private val httpClient: HttpClient,
	private val objectMapper: ObjectMapper,
) {
	fun prepareAndRunSecureHttpRequest(
		url: String,
		token: String? = null,
		httpMethod: HttpMethod = HttpMethod.GET,
		contentType: ContentType = ContentType.APPLICATION_JSON,
		body: String? = null,
		headers: Map<HttpHeader, String> = emptyMap(),
		silent: Boolean = false
	): JsonNode? {
		val httpRequest = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.method(
				httpMethod.asString(),
				body?.let { BodyPublishers.ofString(it) } ?: BodyPublishers.noBody(),
			)
			.header(HttpHeader.CONTENT_TYPE.asString(), contentType.mimeType)
			.apply { token?.let { header(HttpHeader.AUTHORIZATION.asString(), token) } }
			.apply { headers.forEach { header(it.key.asString(), it.value) } }
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
