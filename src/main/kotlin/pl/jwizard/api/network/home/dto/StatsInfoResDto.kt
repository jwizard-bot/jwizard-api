/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.home.dto

data class StatsInfoResDto(
	val modules: Int,
	val commands: Int,
	val radioStations: Int,
	val audioSources: Int,
)
