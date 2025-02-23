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

		private const val DEFAULT_CACHE_GROUP = "jwizardCacheGroup"
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
		cacheKey: CacheEntity,
		computeOnAbsent: () -> T,
		revalidateData: (data: T) -> Boolean = { true },
	): T {
		val cache = getCache()
		var cachedData = cache.get(cacheKey.key, T::class.java)
		if (cachedData != null) {
			if (revalidateData(cachedData)) {
				log.debug("Getting from cache entity: {}.", cacheKey.key)
				return cachedData
			} else {
				deleteFromCache(cacheKey, cacheKey.key)
			}
		}
		cachedData = computeOnAbsent()
		cache.put(cacheKey.key, cachedData)
		log.debug("Persisted to cache: {}.", cacheKey.key)
		return cachedData
	}

	final inline fun <reified T : Serializable> getCachedList(
		cacheKey: CacheEntity,
		computeOnAbsent: () -> List<T>,
		revalidateData: (data: List<T>) -> Boolean = { true },
	): List<T> {
		val cache = getCache()
		var cachedData = cache.get(cacheKey.key, CacheListContainer::class.java)
		if (cachedData != null) {
			val (elements) = cachedData
			val castedElements = elements.map { T::class.java.cast(it) }
			if (revalidateData(castedElements)) {
				log.debug("Getting: {} values from cache entity: {}.", elements.size, cacheKey.key)
				return castedElements
			} else {
				deleteFromCache(cacheKey, cacheKey.key)
			}
		}
		cachedData = CacheListContainer(elements = computeOnAbsent())
		cache.put(cacheKey.key, cachedData)
		val (elements) = cachedData
		log.debug("Persisted: {} values to cache: {}.", elements.size, cacheKey.key)
		return elements.map { T::class.java.cast(it) }
	}

	fun deleteFromCache(cacheEntity: CacheEntity, key: Any) {
		val cache = cacheManager.getCache(cacheEntity.key) ?: return
		cache.evict(key)
		log.debug("Deleted cache: {} with key: {}.", cacheEntity.key, key)
	}

	// should never throw, but who knows
	fun getCache() = cacheManager.getCache(DEFAULT_CACHE_GROUP)
		?: throw RuntimeException("Unable to find cache with name $DEFAULT_CACHE_GROUP.")
}
