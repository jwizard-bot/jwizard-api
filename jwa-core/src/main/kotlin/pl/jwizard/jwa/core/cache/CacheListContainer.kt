package pl.jwizard.jwa.core.cache

import java.io.Serializable

data class CacheListContainer<T : Serializable>(
	val elements: List<T>,
) : Serializable
