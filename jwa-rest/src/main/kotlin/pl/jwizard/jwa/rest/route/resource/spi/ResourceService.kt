package pl.jwizard.jwa.rest.route.resource.spi

import java.io.InputStream

interface ResourceService {
	fun getResource(name: String): Pair<String, InputStream>?
}
