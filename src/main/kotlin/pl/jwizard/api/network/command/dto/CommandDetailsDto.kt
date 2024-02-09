/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.command.dto

data class CommandsResDto(
	val categories: Map<String, String>,
	val commmands: Map<String, CommandDetailsDto>,
	val modules: Map<String, String>,
)

data class CommandDetailsDto(
	val aliases: List<String>,
	val category: String,
	val desc: String,
	val argsDesc: String?,
	val args: List<CommandArgumentDto>
)

data class CommandArgumentDto(
	val id: String,
	val name: String,
	val type: String,
	val req: Boolean,
)
