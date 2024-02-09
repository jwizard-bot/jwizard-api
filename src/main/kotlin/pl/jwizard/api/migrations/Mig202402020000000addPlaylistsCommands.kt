/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.migrations

import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import pl.jwizard.api.domain.guild.GuildRepository

@ChangeUnit(id = "20240202_0000000_addPlaylistsCommands", order = "001", author = "milosz08", systemVersion = "1")
class Mig202402020000000addPlaylistsCommands {
	private val playlistCommands = arrayOf(
		"addtrackpl",
		"addqueuepl",
		"addplaylist",
		"playpl",
		"showmempl",
		"showmypl",
		"showplsongs",
	)
	private val newModules = arrayOf(
		"settings"
	)

	@Execution
	fun migrate(guildRepository: GuildRepository) {
		val guilds = guildRepository.findAll()
		for (guild in guilds) {
			val mutatedGuild = guild.copy(
				enabledCommands = guild.enabledCommands + playlistCommands,
				enabledSlashCommands = guild.enabledCommands + playlistCommands,
				enabledModules = guild.enabledModules + newModules,
			)
			guildRepository.save(mutatedGuild)
		}
	}

	@RollbackExecution
	fun rollback() {
	}
}
