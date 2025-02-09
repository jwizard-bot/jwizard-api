/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.command.spi

import pl.jwizard.jwa.rest.dto.OptionsResDto
import pl.jwizard.jwa.rest.route.command.dto.CommandDetailsResDto
import pl.jwizard.jwa.rest.route.command.dto.CommandModuleResDto
import pl.jwizard.jwa.rest.route.command.dto.ModuleWithCommandsResDto

/**
 * Service interface for managing command-related operations, including retrieving available command modules, fetching
 * command details, and obtaining a list of commands grouped by modules.
 *
 * @author Miłosz Gilga
 */
interface CommandService {

	/**
	 * Retrieves a list of available command modules, optionally filtered by language.
	 *
	 * @param language The preferred language for the response (optional).
	 * @return An [OptionsResDto] containing a list of command modules.
	 */
	fun getModules(language: String?): OptionsResDto<CommandModuleResDto>

	/**
	 * Retrieves all commands grouped by their respective modules, optionally filtered by language.
	 *
	 * @param language The preferred language for the response (optional).
	 * @return A list of [ModuleWithCommandsResDto] containing commands categorized by modules.
	 */
	fun getCommands(language: String?): List<ModuleWithCommandsResDto>

	/**
	 * Retrieves detailed information about a specific command by its unique identifier.
	 *
	 * @param nameId The unique identifier (slug) of the command.
	 * @param language The preferred language for the response (optional).
	 * @return A [CommandDetailsResDto] containing detailed command information, or null if not found.
	 */
	fun getCommandDetails(nameId: String, language: String?): CommandDetailsResDto?
}
