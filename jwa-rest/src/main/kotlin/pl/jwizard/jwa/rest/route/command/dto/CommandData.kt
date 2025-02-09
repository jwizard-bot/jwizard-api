/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.command.dto

/**
 * Represents metadata for a command, including its name, description, arguments, and different usage formats (legacy
 * and slash commands).
 *
 * @property name The display name of the command.
 * @property slug A unique identifier (slug) for the command.
 * @property description A brief description of what the command does.
 * @property arguments A list of arguments that the command accepts.
 * @property legacyUsage A list of legacy usage patterns for the command.
 * @property slashUsage A list of slash command usage patterns (optional).
 * @author Miłosz Gilga
 */
data class CommandData(
	val name: String,
	val slug: String,
	val description: String,
	val arguments: List<CommandArgument>,
	val legacyUsage: List<String>,
	val slashUsage: List<String>?,
)
