package pl.jwizard.jwa.service.command

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.i18n.I18nUtilLocaleSource
import pl.jwizard.jwa.rest.dto.OptionsResDto
import pl.jwizard.jwa.rest.route.command.CommandService
import pl.jwizard.jwa.rest.route.command.dto.*
import pl.jwizard.jwa.service.instance.BotInstancesService
import pl.jwizard.jwl.command.Command
import pl.jwizard.jwl.command.CommandFormatContext
import pl.jwizard.jwl.command.Module
import pl.jwizard.jwl.i18n.I18n
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.property.BaseEnvironment
import java.util.*

@Component
internal class CommandServiceImpl(
	private val i18n: I18n,
	private val botInstancesService: BotInstancesService,
	environment: BaseEnvironment,
) : CommandService {
	private val legacyPrefix = environment.getProperty<String>(AppBaseProperty.GUILD_LEGACY_PREFIX)

	override fun getModules(language: String?): OptionsResDto<CommandModuleResDto> {
		val commandModules = mutableListOf<CommandModuleResDto>()
		val defaultOption = CommandModuleResDto(
			id = "all",
			name = i18n.t(I18nUtilLocaleSource.ALL, language),
			commandsCount = Command.entries.size,
		)
		val sortedCommandModules = Module.entries.map { module ->
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

	override fun getCommands(language: String?): List<ModuleWithCommandsResDto> {
		val commandModules = mutableListOf<ModuleWithCommandsResDto>()
		for (module in Module.entries) {
			val commandData = mutableListOf<CommandData>()
			for (command in Command.entries.filter { it.module == module }) {
				commandData += CommandData(
					name = command.asString,
					slug = command.asSlug,
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

	override fun getCommandDetails(nameId: String, language: String?): CommandDetailsResDto? {
		val command = Command.entries.find { it.asSlug == nameId } ?: return null
		return CommandDetailsResDto(
			name = command.asString,
			description = i18n.t(command, language),
			arguments = getCommandArguments(command, language),
			legacyUsage = createLegacyUsage(command, language),
			slashUsage = createSlashUsage(command, language),
		)
	}

	private fun getCommandArguments(
		command: Command,
		language: String?,
	) = command.exactArguments.map {
		CommandArgument(
			name = i18n.t(it, language).replace("-", " "),
			type = i18n.t(it.type, language),
			required = it.required,
		)
	}

	private fun createLegacyUsage(
		command: Command,
		language: String?,
	) = botInstancesService.botInstances.keys.map { id ->
		val legacyCommandContext =
			CommandFormatContextImpl("$legacyPrefix${id + 1} ", isSlashEvent = false)
		buildCommandUsageInfo(command, legacyCommandContext, language)
	}

	private fun createSlashUsage(command: Command, language: String?): List<String>? {
		if (!command.slashAvailable) {
			return null
		}
		val slashCommandContext = CommandFormatContextImpl("/", true)
		return listOf(buildCommandUsageInfo(command, slashCommandContext, language))
	}

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
