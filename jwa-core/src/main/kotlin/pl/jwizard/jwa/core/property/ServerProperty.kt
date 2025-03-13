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

	// discord oauth
	DISCORD_OAUTH_APP_ID("discord.oauth.app-id"),
	DISCORD_OAUTH_APP_SECRET("discord.oauth.app-secret"),
	DISCORD_OAUTH_REDIRECT_URL_SUCCESS("discord.oauth.redirect-url.success"),
	DISCORD_OAUTH_REDIRECT_URL_ERROR("discord.oauth.redirect-url.error"),
	DISCORD_OAUTH_SESSION_TTL_SEC("discord.oauth.session_ttl_sec", Int::class),

	// geolocation api
	GEOLOCATION_API_URL("geolocation.api.url"),
	GEOLOCATION_API_KEY("geolocation.api.key"),
	GEOLOCATION_API_NAME("geolocation.api.name"),
	GEOLOCATION_API_WEBSITE("geolocation.api.website"),

	// lavalink
	LAVALINK_API_VERSION("lavalink.api.version"),

	// cache
	CACHE_INVALIDATE_TIME_SEC("cache.invalidate-time-sec", Long::class),
	CACHE_MAX_ELEMENTS("cache.max-elements", Long::class),

	// server
	SERVER_AES_SECRET_KEY("server.aes-secret-key"),

	// yauaa
	YAUAA_CACHE_MAX_ELEMENTS("yauaa.cache-max-elements", Int::class),
	;
}
