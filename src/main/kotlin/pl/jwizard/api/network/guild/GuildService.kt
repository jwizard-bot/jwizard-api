/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.guild

import pl.jwizard.api.network.guild.dto.StandaloneGuildDetailsResDto

interface GuildService {
	fun createOrGetGuildSettings(guildId: String): StandaloneGuildDetailsResDto
	fun deletePersistedMusicChannel(guildId: String)
	fun deleteGuildSettings(guildId: String)
}
