/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core

import com.fasterxml.jackson.databind.ObjectMapper
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.util.logger
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

	companion object {
		private val log = logger<HttpClientFacadeBean>()
	}

	/**
	 * The HTTP client used for making requests to external APIs. This client is initialized during class instantiation.
	 */
	private final val httpClient = HttpClient.newHttpClient()

	init {
		log.info("Init HttpClient for external API calls.")
	}

	/**
	 * Makes an HTTP GET request to the specified URL and returns a list of JSON objects as a list of mutable maps
	 * (`List<MutableMap<String, Any>>`). The response is expected to be a JSON array, which is deserialized into the
	 * return type.
	 *
	 * @param url The URL to send the GET request to.
	 * @return A list of mutable maps representing the JSON response body.
	 * @throws RuntimeException if the HTTP status code is not 200 or if an error occurs during the request.
	 */
	fun getJsonListCall(url: String): List<MutableMap<String, Any>> {
		val httpRequest = HttpRequest.newBuilder()
			.uri(URI(url))
			.build()

		val response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
		if (response.statusCode() != 200) {
			throw RuntimeException("Could not perform call: ${response.uri()}. Ended code: ${response.statusCode()}.")
		}
		val reader = objectMapper.readerFor(List::class.java)
		return reader.readValue(response.body())
	}
}
