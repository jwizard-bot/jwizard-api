/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.deployment.dto

data class DeploymentResDto(
	val version: String,
	val lastBuildDate: String,
)