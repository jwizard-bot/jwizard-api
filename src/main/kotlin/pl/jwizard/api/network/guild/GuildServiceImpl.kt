/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.guild

import org.bson.types.ObjectId
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import pl.jwizard.api.domain.guild.*
import pl.jwizard.api.exception.app.GuildException
import pl.jwizard.api.network.command.CommandCategory
import pl.jwizard.api.network.command.CommandProperties
import pl.jwizard.api.network.guild.dto.StandaloneGuildDetailsResDto
import pl.jwizard.api.scaffold.AbstractLoggingBean

@Service
class GuildServiceImpl(
	private val guildDefaultProperties: GuildDefaultProperties,
	private val commandProperties: CommandProperties,
	private val guildRepository: GuildRepository,
	private val modelMapper: ModelMapper,
) : AbstractLoggingBean(GuildServiceImpl::class), GuildService {

	override fun createOrGetGuildSettings(guildId: String): StandaloneGuildDetailsResDto {
		val guildDocument = guildRepository.findByGuildId(guildId).orElseGet {
			val commands = CommandCategory.entries
				.map { it.commandSupplier(commandProperties) }
				.flatMap { (_, commands) -> commands.map { it.name } }
			val guild = modelMapper.map(guildDefaultProperties, GuildDocument::class.java).copy(
				id = ObjectId(),
				enabledCommands = commands,
				enabledSlashCommands = commands,
				inactivity = Inactivity(guildDefaultProperties.inactivity),
				voting = Voting(guildDefaultProperties.voting),
				audioPlayer = AudioPlayer(guildDefaultProperties.audioPlayer),
				enabledModules = guildDefaultProperties.modules,
				musicTextChannelId = null,
				guildId = guildId,
			)
			val persistedSettings = guildRepository.save(guild)
			log.info("Successfully persisted guild settings: {}", persistedSettings)
			persistedSettings
		}
		return modelMapper.map(guildDocument, StandaloneGuildDetailsResDto::class.java)
	}

	override fun deletePersistedMusicChannel(guildId: String) {
		val guildDocument = guildRepository
			.findByGuildId(guildId)
			.orElseThrow { throw GuildException.GuildNotExistException(guildId) }

		val mutabledGuildDocument = guildDocument.copy(
			musicTextChannelId = null
		)
		guildRepository.save(mutabledGuildDocument)
		log.info(
			"Successfully removed saved music text channel ID and return to default value (null) for guild: {}",
			guildDocument
		)
	}

	override fun deleteGuildSettings(guildId: String) {
		if (!guildRepository.existsByGuildId(guildId)) {
			throw GuildException.GuildNotExistException(guildId)
		}
		guildRepository.deleteByGuildId(guildId)
		log.info("Successfully deleted guild settings for guild ID: {}", guildId)
	}
}
