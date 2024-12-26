/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.cache

import pl.jwizard.jwl.property.Property

/**
 * Enum class representing different cache entities used within the application. Each enum constant corresponds to a
 * specific cache and is associated with a unique cache key.
 *
 * @property key The unique key identifying the cache entity.
 * @author Miłosz Gilga
 */
enum class CacheEntity(override val key: String) : Property {
	CONTRIBUTORS("Contributors.Cache"),
	PROJECT_PACKAGES("ProjectPackages.Cache"),
	;
}
