/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.identity.standalone.dto

data class TokenDataResDto(
	val accessToken: String,
	val refreshToken: String,
)
