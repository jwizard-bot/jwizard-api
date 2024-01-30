/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.domain.refreshtoken

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "refresh_tokens")
data class RefreshTokenDocument(
	@Id
	val id: ObjectId = ObjectId(),

	val refreshToken: String,

	val expiredAt: LocalDateTime,

	val userDcId: String,
)
