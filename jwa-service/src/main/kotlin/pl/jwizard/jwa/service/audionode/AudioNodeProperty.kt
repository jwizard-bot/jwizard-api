package pl.jwizard.jwa.service.audionode

import pl.jwizard.jwl.vault.kvgroup.VaultKvGroupPropertySource
import kotlin.reflect.KClass

internal enum class AudioNodeProperty(
	override val key: String,
	override val type: KClass<*> = String::class,
) : VaultKvGroupPropertySource {
	ACTIVE("V_ACTIVE", Boolean::class),
	NAME("V_NAME"),
	GATEWAY_HOST("V_GATEWAY_HOST"),
	PASSWORD("V_PASSWORD"),
	REGION_GROUP("V_REGION_GROUP"),
	NODE_POOL("V_NODE_POOL"),
	SECURE("V_SECURE", Boolean::class),
	;
}
