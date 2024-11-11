/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * Facade class for making HTTP requests using Java's [HttpClient] API.
 *
 * This class provides a simplified interface for making HTTP GET requests that return JSON data, which is then
 * deserialized into a list of maps. It uses an [ObjectMapper] to deserialize the JSON response into the appropriate
 * data structures.
 *
 * @property objectMapper The Jackson [ObjectMapper] used to deserialize JSON responses.
 * @author Miłosz Gilga
 */
@SingletonComponent
class HttpClientFacadeBean(private val objectMapper: ObjectMapper) {

	/**
	 * The HTTP client used for making requests to external APIs. This client is initialized during class instantiation.
	 */
	private final val httpClient = HttpClient.newHttpClient()

	/**
	 * Sends an HTTP GET request to the specified URL and parses the JSON response into a [JsonNode].
	 *
	 * This method creates an HTTP GET request to fetch JSON data from the specified URL. The response body is then
	 * deserialized into a [JsonNode] using the [ObjectMapper]. If the response status code is not 200, it throws
	 * a [RuntimeException].
	 *
	 * @param url The URL to send the GET request to. It should point to a JSON resource.
	 * @return A [JsonNode] representing the JSON data returned by the response.
	 * @throws RuntimeException If the HTTP status code is not 200 or if an error occurs during the request.
	 */
	fun getJsonListCall(url: String): JsonNode {
		val httpRequest = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.build()
		val response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
		if (response.statusCode() != 200) {
			throw RuntimeException("Could not perform call: ${response.uri()}. Ended code: ${response.statusCode()}.")
		}
		return objectMapper.readTree(response.body())
	}
}
