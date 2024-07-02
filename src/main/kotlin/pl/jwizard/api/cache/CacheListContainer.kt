/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.cache

import java.io.Serializable

data class CacheListContainer<T>(
	val elements: List<T>,
) : Serializable
