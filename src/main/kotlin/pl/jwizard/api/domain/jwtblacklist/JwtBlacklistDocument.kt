/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.domain.jwtblacklist

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "jwts_blacklist")
class JwtBlacklistDocument(
	@Id
	val id: ObjectId = ObjectId(),

	val jwt: String,

	val expiredAt: LocalDateTime,
)
