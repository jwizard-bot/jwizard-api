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
	fun findByUserDcId(userDcId: String): RefreshTokenDocument?
	fun findByRefreshTokenAndUserDcId(refreshToken: String, userDcId: String): RefreshTokenDocument?
}
