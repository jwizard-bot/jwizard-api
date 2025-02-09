/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.i18n.I18nUtilLocaleSource
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.rest.dto.OptionsResDto
import pl.jwizard.jwa.rest.route.command.dto.*
import pl.jwizard.jwa.rest.route.command.spi.CommandService
import pl.jwizard.jwl.command.Command
import pl.jwizard.jwl.command.CommandFormatContext
import pl.jwizard.jwl.command.CommandFormatContextImpl
import pl.jwizard.jwl.command.Module
import pl.jwizard.jwl.i18n.I18nBean
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseProperty
import java.util.*

/**
 * Service bean responsible for providing command-related functionalities, including retrieving command modules,
 * fetching command details, and formatting command usage information.
 *
 * @property environment The environment configuration used to retrieve property values.
 * @property i18n The internationalization service used for translating strings based on language preferences.
 * @property botInstancesService The service providing information about bot instances.
 * @author Miłosz Gilga
 */
@SingletonService
class CommandServiceBean(
	private val environment: EnvironmentBean,
	private val i18n: I18nBean,
	private val botInstancesService: BotInstancesServiceBean,
) : CommandService {

	/**
	 * The prefix used for legacy commands.
	 */
	private val legacyPrefix = environment.getProperty<String>(AppBaseProperty.GUILD_LEGACY_PREFIX)

	/**
	 * Retrieves a list of command modules and returns them as an [OptionsResDto].
	 *
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 * @return An [OptionsResDto] containing the list of command modules.
	 */
	override fun getModules(language: String?): OptionsResDto<CommandModuleResDto> {
		val commandModules = mutableListOf<CommandModuleResDto>()
		val defaultOption = CommandModuleResDto(
			id = "all",
			name = i18n.t(I18nUtilLocaleSource.ALL, language),
			commandsCount = Command.entries.size,
		)
		val sortedCommandModules = Module.entries
			.map { module ->
				CommandModuleResDto(
					id = module.textKey,
					name = i18n.t(module, language),
					commandsCount = Command.entries.count { it.module == module },
				)
			}.sortedBy { it.name }

		commandModules += defaultOption
		commandModules += sortedCommandModules

		return OptionsResDto(
			defaultOption,
			options = commandModules,
		)
	}

	/**
	 * Retrieves all commands grouped by their respective modules and returns them as a list of [ModuleWithCommandsResDto].
	 *
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 * @return A list of [ModuleWithCommandsResDto] containing the commands organized by module.
	 */
	override fun getCommands(language: String?): List<ModuleWithCommandsResDto> {
		val commandModules = mutableListOf<ModuleWithCommandsResDto>()
		for (module in Module.entries) {
			val commandData = mutableListOf<CommandData>()
			for (command in Command.entries.filter { it.module == module }) {
				commandData += CommandData(
					name = command.toNameWithSpaces,
					slug = command.toUrl,
					description = i18n.t(command, language),
					arguments = getCommandArguments(command, language),
					legacyUsage = createLegacyUsage(command, language),
					slashUsage = createSlashUsage(command, language),
				)
			}
			commandModules += ModuleWithCommandsResDto(
				id = module.textKey,
				name = i18n.t(module, language),
				commands = commandData.sortedBy { it.name },
			)
		}
		return commandModules.sortedBy { it.name }
	}

	/**
	 * Retrieves detailed information about a specific command by its unique name identifier.
	 *
	 * @param nameId The unique identifier (slug) of the command.
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 * @return A [CommandDetailsResDto] containing detailed command information, or null if the command is not found.
	 */
	override fun getCommandDetails(nameId: String, language: String?): CommandDetailsResDto? {
		val command = Command.entries.find { it.toUrl == nameId } ?: return null
		return CommandDetailsResDto(
			name = command.toNameWithSpaces,
			description = i18n.t(command, language),
			arguments = getCommandArguments(command, language),
			legacyUsage = createLegacyUsage(command, language),
			slashUsage = createSlashUsage(command, language),
		)
	}

	/**
	 * Retrieves the arguments for a specific command, including name, type, and whether the argument is required.
	 *
	 * @param command The command whose arguments are to be fetched.
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 * @return A list of [CommandArgument] representing the arguments of the command.
	 */
	private fun getCommandArguments(command: Command, language: String?) = command.exactArguments.map {
		CommandArgument(
			name = i18n.t(it, language).replace("-", " "),
			type = i18n.t(it.type, language),
			required = it.required,
		)
	}

	/**
	 * Creates the legacy usage patterns for a given command, based on the bot instances.
	 *
	 * @param command The command whose legacy usage patterns are to be generated.
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 * @return A list of strings representing the legacy command usage patterns.
	 */
	private fun createLegacyUsage(command: Command, language: String?) = botInstancesService.botInstances.keys
		.map { id ->
			val legacyCommandContext = CommandFormatContextImpl("$legacyPrefix${id + 1} ", isSlashEvent = false)
			buildCommandUsageInfo(command, legacyCommandContext, language)
		}

	/**
	 * Creates the slash usage pattern for a given command, if applicable.
	 *
	 * @param command The command whose slash usage pattern is to be generated.
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 * @return A list containing the slash usage pattern for the command, or null if the command is not available for
	 *         slash usage.
	 */
	private fun createSlashUsage(command: Command, language: String?): List<String>? {
		if (!command.slashAvailable) {
			return null
		}
		val slashCommandContext = CommandFormatContextImpl("/", true)
		return listOf(buildCommandUsageInfo(command, slashCommandContext, language))
	}

	/**
	 * Builds the command usage information as a string, including any arguments and syntax.
	 *
	 * @param command The command whose usage is being formatted.
	 * @param commandFormatContext The context used for command formatting (ex. legacy or slash).
	 * @param language The optional language code (ex. "en", "pl") to determine localization.
	 * @return A string representing the formatted command usage.
	 */
	private fun buildCommandUsageInfo(
		command: Command,
		commandFormatContext: CommandFormatContext,
		language: String?,
	): String {
		val stringJoiner = StringJoiner(" ")
		stringJoiner.add(command.parseWithPrefix(commandFormatContext))

		if (command.argumentsDefinition != null) {
			val argSyntax = i18n.t(command.argumentsDefinition!!, language)
			stringJoiner.add("<$argSyntax>")
		}
		return stringJoiner.toString()
	}
}
