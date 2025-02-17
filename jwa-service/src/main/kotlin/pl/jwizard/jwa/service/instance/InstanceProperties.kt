package pl.jwizard.jwa.service.instance

import pl.jwizard.jwl.property.PropertyNotFoundException
import pl.jwizard.jwl.util.castToValue
import java.util.*

internal class InstanceProperties(incomingProperties: Properties) {
	private val properties = mutableMapOf<String, String>()

	init {
		val initValues = incomingProperties.entries.associate {
			it.key.toString() to it.value.toString()
		}
		properties.putAll(initValues)
	}

	inline fun <reified T> get(instanceProperty: InstanceProperty): T {
		val rawProperty = properties[instanceProperty.key]
			?: throw PropertyNotFoundException(this::class, instanceProperty.key)
		return castToValue(rawProperty, instanceProperty.type) as T
	}
}
