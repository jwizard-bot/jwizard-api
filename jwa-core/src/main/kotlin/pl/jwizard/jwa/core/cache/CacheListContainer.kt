/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.cache

import java.io.Serializable

/**
 * A container for storing a list of cached elements.
 *
 * This class is used to wrap a list of elements that are stored in the cache. It ensures that a list of items can be
 * cached together and easily retrieved later. The class is serializable, allowing it to be stored in a cache such as
 * Caffeine, which requires objects to be serializable.
 *
 * @param T The type of the elements in the list, which must be serializable.
 * @property elements A list of elements to be cached.
 * @author Miłosz Gilga
 */
data class CacheListContainer<T : Serializable>(
	val elements: List<T>,
) : Serializable
