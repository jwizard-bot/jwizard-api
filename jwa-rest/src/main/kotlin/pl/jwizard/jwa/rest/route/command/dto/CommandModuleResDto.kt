/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.command.dto

/**
 * Represents a response DTO containing information about a command module. It includes the module's unique identifier,
 * name, and the number of commands it contains.
 *
 * @property id The unique identifier of the command module.
 * @property name The display name of the command module.
 * @property commandsCount The total number of commands available in this module.
 * @author Miłosz Gilga
 */
data class CommandModuleResDto(
	val id: String,
	val name: String,
	val commandsCount: Int,
)
