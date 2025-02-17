package pl.jwizard.jwa.core.cache

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.util.logger
import java.io.Serializable
import java.util.concurrent.TimeUnit

@Component
class CacheFacade(environment: BaseEnvironment) {
	companion object {
		val log = logger<CacheFacade>()
	}

	private final val cacheManager: CacheManager

	init {
		val cacheInvalidateTimeSec = environment
			.getProperty<Long>(ServerProperty.CACHE_INVALIDATE_TIME_SEC)
		val cacheMaxElements = environment.getProperty<Long>(ServerProperty.CACHE_MAX_ELEMENTS)

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

	final inline fun <reified T : Serializable> getCached(
		cacheEntity: CacheEntity,
		key: Any,
		computeOnAbsent: () -> T,
		revalidateData: (data: T) -> Boolean = { true },
	): T {
		val cache = getCache(cacheEntity)
		var cachedData = cache.get(key, T::class.java)
		if (cachedData != null) {
			if (revalidateData(cachedData)) {
				log.debug("Getting from cache entity: {} with key: {}.", cacheEntity.key, key)
				return cachedData
			} else {
				deleteFromCache(cacheEntity, key)
			}
		}
		cachedData = computeOnAbsent()
		cache.put(key, cachedData)
		log.debug("Persisted to cache: {} with key: {}.", cacheEntity.key, key)
		return cachedData
	}

	final inline fun <reified T : Serializable> getCachedList(
		cacheEntity: CacheEntity,
		key: Any,
		computeOnAbsent: () -> List<T>,
		revalidateData: (data: List<T>) -> Boolean = { true },
	): List<T> {
		val cache = getCache(cacheEntity)
		var cachedData = cache.get(key, CacheListContainer::class.java)
		if (cachedData != null) {
			val (elements) = cachedData
			val castedElements = elements.map { T::class.java.cast(it) }
			if (revalidateData(castedElements)) {
				log.debug(
					"Getting: {} values from cache entity: {} with key: {}.",
					elements.size,
					cacheEntity.key,
					key
				)
				return castedElements
			} else {
				deleteFromCache(cacheEntity, key)
			}
		}
		cachedData = CacheListContainer(elements = computeOnAbsent())
		cache.put(key, cachedData)
		val (elements) = cachedData
		log.debug(
			"Persisted: {} values to cache: {} with key: {}.",
			elements.size,
			cacheEntity.key,
			key
		)
		return elements.map { T::class.java.cast(it) }
	}

	fun deleteFromCache(cacheEntity: CacheEntity, key: Any) {
		val cache = cacheManager.getCache(cacheEntity.key) ?: return
		cache.evict(key)
		log.debug("Deleted cache: {} with key: {}.", cacheEntity.key, key)
	}

	fun getCache(
		cacheEntity: CacheEntity,
	) = cacheManager.getCache(cacheEntity.key)
		?: throw RuntimeException("Unable to find cache with name ${cacheEntity.key}.")
}
