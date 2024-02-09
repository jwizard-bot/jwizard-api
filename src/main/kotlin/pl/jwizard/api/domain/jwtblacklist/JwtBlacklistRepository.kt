/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.domain.jwtblacklist

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface JwtBlacklistRepository : MongoRepository<JwtBlacklistDocument, ObjectId> {
	fun existsByJwt(jwt: String): Boolean
	fun deleteAllByExpiredAtBefore(now: LocalDateTime)
}
