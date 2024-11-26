/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import io.javalin.http.NotFoundResponse
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.rest.resource.spi.ResourceService
import pl.jwizard.jwl.file.IndependentFileBrowser
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseListProperty
import pl.jwizard.jwl.util.findResourceInMultipleContainers
import java.io.IOException
import java.io.InputStream

/**
 * Service class responsible for managing access to static resources.
 *
 * This service handles retrieving resources stored in the application's classpath. It supports returning the resource
 * along with its content type for further processing or serving in HTTP responses.
 *
 * @property environment Provides access to environment properties, including configuration for the resources' path.
 * @author Miłosz Gilga
 */
@SingletonService
class ResourceServiceBean(private val environment: EnvironmentBean) : ResourceService {

	/**
	 * List of prefixes that define the possible directories where static resources may be located.
	 */
	private val prefixes = environment.getListProperty<String>(AppBaseListProperty.STATIC_RESOURCES_PREFIXES)

	/**
	 * A flattened list of all available resources, obtained by browsing all directories defined by the prefixes.
	 */
	private val availableResources = prefixes
		.map { IndependentFileBrowser(it).listAllFilesFromDirectory() }
		.flatten()

	/**
	 * Retrieves a resource by its name. If the resource does not exist, a NotFoundResponse is thrown. The resource is
	 * looked up in multiple containers (directories) based on the predefined prefixes.
	 *
	 * @param name The name of the resource to retrieve.
	 * @return A pair containing the content type of the resource and its input stream for reading.
	 * @throws NotFoundResponse If the resource does not exist in any of the defined directories.
	 */
	override fun getResource(name: String): Pair<String, InputStream> {
		if (!availableResources.contains(name)) {
			throw NotFoundResponse()
		}
		val classPathResource = findResourceInMultipleContainers(prefixes, name) ?: throw NotFoundResponse()
		return try {
			val connection = classPathResource.url.openConnection()
			Pair(connection.contentType ?: "application/octet-stream", classPathResource.inputStream)
		} catch (ex: IOException) {
			throw NotFoundResponse()
		}
	}
}
