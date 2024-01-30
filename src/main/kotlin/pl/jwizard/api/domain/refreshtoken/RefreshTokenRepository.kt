/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.domain.refreshtoken

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : MongoRepository<RefreshTokenDocument, ObjectId> {
	fun findByRefreshTokenAndUserDcId(refreshToken: String, serDcId: String): RefreshTokenDocument?
	fun existsByRefreshTokenAndUserDcId(refreshToken: String, serDcId: String): Boolean
	fun deleteByRefreshTokenAndUserDcId(refreshToken: String, serDcId: String)
}
