/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.resource.spi

import io.javalin.http.NotFoundResponse
import java.io.InputStream

/**
 * Service interface for accessing static resources.
 *
 * This interface provides a method to retrieve resources stored in the application's classpath or other configured
 * storage, returning both the resource content type and its data as an input stream. Implementations of this interface
 * are expected to handle resource retrieval and error handling for missing resources.
 *
 * @author Miłosz Gilga
 */
interface ResourceService {

	/**
	 * Retrieves a specified resource by name, including its content type and data stream.
	 *
	 * Implementing this method should involve locating the resource by its name, determining its content type (ex.
	 * `image/png`, `text/plain`), and providing an [InputStream] to access its content. If the resource cannot be found,
	 * it is expected that the implementation will handle this appropriately, such as by throwing an exception.
	 *
	 * @param name The name of the resource file to be retrieved.
	 * @return A [Pair] where the first element is the content type of the resource as a [String], and the second
	 *         element is an input stream for reading the resource data.
	 * @throws NotFoundResponse If the resource cannot be found, an exception may be thrown.
	 */
	fun getResource(name: String): Pair<String, InputStream>
}
