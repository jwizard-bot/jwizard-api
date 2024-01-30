/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.identity.standalone.dto

import jakarta.validation.constraints.NotBlank

data class RefreshReqDto(
	@field:NotBlank(message = "Expired access token field cannot be blank")
	val expiredAccessToken: String,

	@field:NotBlank(message = "Refresh token field cannot be blank")
	val refreshToken: String,
)

