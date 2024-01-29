/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security.jwt

import io.jsonwebtoken.Claims
import java.util.*

data class TokenData(
	val token: String,
	val expiredAt: Date,
)

data class JwtValidateState(
	val state: JwtState,
	val claims: Claims?,
)
