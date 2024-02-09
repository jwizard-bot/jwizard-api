/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.migrations

import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import pl.jwizard.api.domain.guild.GuildRepository

@ChangeUnit(id = "Mig202402030000000addMusicTextChannelId", order = "002", author = "milosz08", systemVersion = "1")
class Mig202402030000001addMusicTextChannelId {

	@Execution
	fun migrate(guildRepository: GuildRepository) {
		val guilds = guildRepository.findAll()
		for (guild in guilds) {
			val mutatedGuild = guild.copy(
				musicTextChannelId = null
			)
			guildRepository.save(mutatedGuild)
		}
	}

	@RollbackExecution
	fun rollback() {
	}
}
