package pl.jwizard.jwa.core.property

import pl.jwizard.jwl.property.AppListProperty
import kotlin.reflect.KClass

enum class ServerListProperty(
	override val key: String,
	override val separator: String? = null,
	override val listElementsType: KClass<*> = String::class,
) : AppListProperty {
	// cors
	CORS_URLS("cors.urls"),

	// discord oauth
	DISCORD_OAUTH_SCOPES("discord.oauth.scopes"),
	;
}
