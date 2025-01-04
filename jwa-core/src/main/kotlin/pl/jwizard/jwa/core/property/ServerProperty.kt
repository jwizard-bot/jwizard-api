/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.property

import pl.jwizard.jwa.core.property.ServerProperty.*
import pl.jwizard.jwl.property.AppProperty
import kotlin.reflect.KClass

/**
 * Enum class representing server-related properties used within the application.
 *
 * Defining following properties:
 * - [GITHUB_API_URL]: The URL for the GitHub API used in the application.
 * - [GITHUB_LANGUAGE_COLOR_API_URL]: The URL for the GitHub Language Color API.
 * - [CACHE_INVALIDATE_TIME_SEC]: The time in seconds for cache invalidation. Used to determine how long cache entries
 *   should be kept before they are invalidated.
 * - [CACHE_MAX_ELEMENTS]: The maximum number of elements allowed in the cache. Used to control the size of the cache.
 *
 * @property key The unique key for the property, which is used to retrieve its value from the configuration.
 * @property type The type of the property value, defaulting to [String] if not specified.
 * @author Miłosz Gilga
 */
enum class ServerProperty(
	override val key: String,
	override val type: KClass<*> = String::class,
) : AppProperty {

	/**
	 * The URL for the GitHub API used in the application.
	 */
	GITHUB_API_URL("github.api-url"),

	/**
	 * The URL for the GitHub Language Color API.
	 */
	GITHUB_LANGUAGE_COLOR_API_URL("github.language-color-api-url"),

	/**
	 * The time in seconds for cache invalidation. Used to determine how long cache entries should be kept before they
	 * are invalidated.
	 */
	CACHE_INVALIDATE_TIME_SEC("cache.invalidate-time-sec", Long::class),

	/**
	 * The maximum number of elements allowed in the cache. Used to control the size of the cache.
	 */
	CACHE_MAX_ELEMENTS("cache.max-elements", Long::class),
	;
}
