package pl.jwizard.jwa.service.instance

import pl.jwizard.jwl.vault.kvgroup.VaultKvGroupPropertySource
import kotlin.reflect.KClass

// server port property available only for DEV environment!
internal enum class InstanceProperty(
	override val key: String,
	override val type: KClass<*> = String::class,
) : VaultKvGroupPropertySource {
	JDA_INSTANCE_PREFIX("V_JDA_INSTANCE_PREFIX"),
	JDA_PRIMARY_COLOR("V_JDA_PRIMARY_COLOR"),
	JDA_SECRET("V_JDA_SECRET"),
	REST_API_TOKEN("V_REST_API_TOKEN"),
	SERVER_PORT("V_SERVER_PORT", Int::class),
	SHARDS_PER_PROCESS("V_SHARDS_PER_PROCESS", Int::class),
	SHARD_OVERALL_MAX("V_SHARD_OVERALL_MAX", Int::class),
	;
}
