/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.cache

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import pl.jwizard.api.scaffold.AbstractLoggingBean
import kotlin.reflect.KClass
import kotlin.reflect.cast

@Service
class SpringCacheService(
	private val cacheManager: CacheManager
) : CacheService, AbstractLoggingBean(SpringCacheService::class) {

	override fun <T : Any> getSafely(
		entity: CacheEntity,
		key: Any,
		clazz: KClass<T>,
		invokeOnAbsent: () -> T
	): T {
		val cache = getCache(entity)
		var cachedData = cache.get(key, clazz.java)
		// cached found, return cached value
		if (cachedData != null) {
			log.debug("Getting value from cache for entity: {} with key: {}", entity.cacheName, key)
			return cachedData
		}
		// otherwise create new cache and insert object to them
		cachedData = invokeOnAbsent()
		cache.put(key, cachedData)
		log.info("Persisted value to cache: {} with key: {}", entity.cacheName, key)
		return cachedData
	}

	override fun <T : Any> getSafelyList(
		entity: CacheEntity,
		key: Any,
		clazz: KClass<T>,
		invokeOnAbsent: () -> List<T>
	): List<T> {
		val cache = getCache(entity)
		var cachedData = cache.get(key, CacheListContainer::class.java)
		if (cachedData != null) {
			val (elements) = cachedData
			log.debug("Getting: {} values from cache for entity: {} with key: {}", elements.size, entity.cacheName, key)
			return elements.map { clazz.cast(it) }
		}
		cachedData = CacheListContainer(
			elements = invokeOnAbsent()
		)
		cache.put(key, cachedData)
		val (elements) = cachedData
		log.info("Persisted: {} values to cache: {} with key: {}", elements.size, entity.cacheName, key)
		return elements.map { clazz.cast(it) }
	}

	override fun delete(entity: CacheEntity, key: Any) {
		val cachedData = cacheManager.getCache(entity.cacheName) ?: return // not found cache, skipping
		cachedData.evict(key)
		log.info("Deleted cache: {} with key: {}", entity.cacheName, key)
	}

	private fun getCache(entity: CacheEntity): Cache = cacheManager.getCache(entity.cacheName)
		?: throw RuntimeException("Unable to find cache with name ${entity.cacheName}")
}
