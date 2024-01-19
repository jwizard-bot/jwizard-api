/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.command.dto

data class CommandsResDto(
	val categories: Map<String, String>,
	val commmands: Map<String, Map<String, CommandDetailsDto>>,
)

data class CommandDetailsDto(
	val aliases: List<String>,
	val description: String,
)

data class CommandDetailsMapperDto(
	val aliases: String,
)
