package pl.jwizard.jwa.core.cache

import java.io.Serializable

data class TimestampedStatus<T : Serializable>(
	val data: T,
	val fetchedAt: Long = System.currentTimeMillis(),
) : Serializable
