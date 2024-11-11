/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.home.dto

/**
 * Data Transfer Object (DTO) representing statistics information of the system.
 *
 * @property modules The number of modules present in the system.
 * @property commands The number of commands available in the system.
 * @property radioStations The number of radio stations integrated into the system.
 * @property openSourceLibraries The number of open-source libraries used in the system.
 * @author Miłosz Gilga
 */
data class StatisticsInfoResDto(
	val modules: Int,
	val commands: Int,
	val radioStations: Int,
	val openSourceLibraries: Int,
)
