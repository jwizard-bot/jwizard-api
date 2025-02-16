package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.rest.route.resource.spi.ResourceService
import pl.jwizard.jwl.file.IndependentFileBrowser
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseListProperty
import pl.jwizard.jwl.util.findResourceInMultipleContainers
import java.io.IOException
import java.io.InputStream

@SingletonService
class ResourceServiceBean(environment: EnvironmentBean) : ResourceService {
	private val prefixes = environment
		.getListProperty<String>(AppBaseListProperty.STATIC_RESOURCES_PREFIXES)

	private val availableResources = prefixes
		.map { IndependentFileBrowser(it).listAllFilesFromDirectory() }
		.flatten()

	override fun getResource(name: String): Pair<String, InputStream>? {
		if (!availableResources.contains(name)) {
			return null
		}
		val classPathResource = findResourceInMultipleContainers(prefixes, name) ?: return null
		return try {
			val connection = classPathResource.url.openConnection()
			Pair(connection.contentType ?: "application/octet-stream", classPathResource.inputStream)
		} catch (ex: IOException) {
			return null
		}
	}
}
