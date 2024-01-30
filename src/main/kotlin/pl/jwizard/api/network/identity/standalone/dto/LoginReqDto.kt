/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.identity.standalone.dto

import jakarta.validation.constraints.NotBlank

data class LoginReqDto(
	@field:NotBlank(message = "App ID field cannot be blank")
	val appId: String,

	@field:NotBlank(message = "App secret field cannot be blank")
	val appSecret: String,
)
