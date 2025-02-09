/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.command.dto

/**
 * Represents a single argument of a command in the system. It holds information about the argument's name, type, and
 * whether it is required.
 *
 * @property name The name of the argument.
 * @property type The data type of the argument.
 * @property required Specifies if the argument is mandatory (true) or optional (false).
 * @author Miłosz Gilga
 */
data class CommandArgument(
	val name: String,
	val type: String,
	val required: Boolean,
)
