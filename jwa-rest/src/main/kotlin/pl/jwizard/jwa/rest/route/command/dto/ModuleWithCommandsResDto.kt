/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.command.dto

/**
 * Represents a response DTO containing detailed information about a command module, including its ID, name, and a list
 * of associated commands.
 *
 * @property id The unique identifier of the command module.
 * @property name The display name of the command module.
 * @property commands A list of commands that belong to this module.
 * @author Miłosz Gilga
 */
data class ModuleWithCommandsResDto(
	val id: String,
	val name: String,
	val commands: List<CommandData>,
)
