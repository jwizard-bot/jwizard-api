package pl.jwizard.jwa.core.property

import pl.jwizard.jwl.property.AppProperty
import kotlin.reflect.KClass

enum class ServerProperty(
	override val key: String,
	override val type: KClass<*> = String::class,
) : AppProperty {
	// github
	GITHUB_API_URL("github.api.url"),
	GITHUB_API_TOKEN("github.api.token"),
	GITHUB_LANGUAGE_COLOR_API_URL("github.language-color-api-url"),

	// discord
	DISCORD_API_URL("discord.api.url"),
	DISCORD_PROCESS_URL_FRAGMENT("discord.process.url-fragment"),

	// lavalink
	LAVALINK_API_VERSION("lavalink.api.version"),

	// cache
	CACHE_INVALIDATE_TIME_SEC("cache.invalidate-time-sec", Long::class),
	CACHE_MAX_ELEMENTS("cache.max-elements", Long::class),
	;
}
