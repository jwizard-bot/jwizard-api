/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.cache

import kotlin.reflect.KClass

interface CacheService {
	fun <T : Any> getSafely(entity: CacheEntity, key: Any, clazz: KClass<T>, invokeOnAbsent: () -> T): T
	fun <T : Any> getSafelyList(entity: CacheEntity, key: Any, clazz: KClass<T>, invokeOnAbsent: () -> List<T>): List<T>
	fun delete(entity: CacheEntity, key: Any)
}
