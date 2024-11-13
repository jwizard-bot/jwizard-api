/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.cache

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.util.logger
import java.io.Serializable
import java.util.concurrent.TimeUnit

/**
 * A facade for managing cache operations using the Caffeine caching library.
 *
 * This class provides methods to interact with a cache, allowing the storage and retrieval of cached values, as well
 * as cache invalidation. The cache manager is configured with a specified cache invalidate time and the maximum number
 * of cache elements, which are provided through the [EnvironmentBean].
 *
 * @property environmentBean An instance of [EnvironmentBean] used to retrieve configuration properties such.
 * @author Miłosz Gilga
 */
@SingletonComponent
class CacheFacadeBean(private val environmentBean: EnvironmentBean) {

	companion object {
		val log = logger<CacheFacadeBean>()
	}

	/**
	 * The cache manager used to interact with Caffeine cache.
	 */
	private final val cacheManager: CacheManager

	init {
		val cacheInvalidateTimeSec = environmentBean.getProperty<Long>(ServerProperty.CACHE_INVALIDATE_TIME_SEC)
		val cacheMaxElements = environmentBean.getProperty<Long>(ServerProperty.CACHE_MAX_ELEMENTS)

		val config = Caffeine.newBuilder()
			.expireAfterWrite(cacheInvalidateTimeSec, TimeUnit.SECONDS)
			.maximumSize(cacheMaxElements)

		cacheManager = CaffeineCacheManager()
		cacheManager.setCaffeine(config)
		log.info(
			"Init cache manager for Caffeine library. Invalidate time (sec): {}, Max elements: {}.",
			cacheInvalidateTimeSec,
			cacheMaxElements
		)
	}

	/**
	 * Retrieves an item from the cache or computes it if absent.
	 *
	 * This method tries to fetch the cached value associated with the given key. If the value is not present, it
	 * computes the value using the provided [computeOnAbsent] lambda, stores it in the cache, and returns it.
	 *
	 * @param T The type of the cached data.
	 * @param cacheEntity The cache entity representing the cache.
	 * @param key The key to identify the cached data.
	 * @param computeOnAbsent A lambda function that computes the value if the key is not found in the cache.
	 * @return The cached data, either from the cache or computed.
	 */
	final inline fun <reified T : Serializable> getCached(
		cacheEntity: CacheEntity,
		key: Any,
		computeOnAbsent: () -> T,
	): T {
		val cache = getCache(cacheEntity)
		var cachedData = cache.get(key, T::class.java)
		if (cachedData != null) {
			log.debug("Getting from cache entity: {} with key: {}.", cacheEntity.key, key)
			return cachedData
		}
		cachedData = computeOnAbsent()
		cache.put(key, cachedData)
		log.debug("Persisted to cache: {} with key: {}.", cacheEntity.key, key)
		return cachedData
	}

	/**
	 * Retrieves a list of items from the cache or computes it if absent.
	 *
	 * This method tries to fetch a list of cached values associated with the given key. If the values are not present,
	 * it computes the list using the provided [computeOnAbsent] lambda, stores it in the cache, and returns the list.
	 *
	 * @param T The type of the cached list items.
	 * @param cacheEntity The cache entity representing the cache.
	 * @param key The key to identify the cached data.
	 * @param computeOnAbsent A lambda function that computes the list if the key is not found in the cache.
	 * @return A list of cached items, either from the cache or computed.
	 */
	final inline fun <reified T : Serializable> getCachedList(
		cacheEntity: CacheEntity,
		key: Any,
		computeOnAbsent: () -> List<T>,
	): List<T> {
		val cache = getCache(cacheEntity)
		var cachedData = cache.get(key, CacheListContainer::class.java)
		if (cachedData != null) {
			val (elements) = cachedData
			log.debug("Getting: {} values from cache entity: {} with key: {}.", elements.size, cacheEntity.key, key)
			return elements.map { T::class.java.cast(it) }
		}
		cachedData = CacheListContainer(elements = computeOnAbsent())
		cache.put(key, cachedData)
		val (elements) = cachedData
		log.debug("Persisted: {} values to cache: {} with key: {}.", elements.size, cacheEntity.key, key)
		return elements.map { T::class.java.cast(it) }
	}

	/**
	 * Deletes an item from the cache.
	 *
	 * This method removes the cached data associated with the given key from the specified cache.
	 *
	 * @param cacheEntity The cache entity representing the cache.
	 * @param key The key of the cached data to be removed.
	 */
	fun deleteFromCache(cacheEntity: CacheEntity, key: Any) {
		val cache = cacheManager.getCache(cacheEntity.key) ?: return
		cache.evict(key)
		log.info("Deleted cache: {} with key: {}.", cacheEntity.key, key)
	}

	/**
	 * Retrieves a specific cache by its entity.
	 *
	 * This method returns the cache associated with the given cache entity.
	 *
	 * @param cacheEntity The cache entity representing the cache.
	 * @return The cache associated with the given entity.
	 * @throws RuntimeException if the cache for the given entity is not found.
	 */
	fun getCache(cacheEntity: CacheEntity) = cacheManager.getCache(cacheEntity.key)
		?: throw RuntimeException("Unable to find cache with name ${cacheEntity.key}.")
}
