/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.guild.dto

import pl.jwizard.api.config.annotation.NoArgConstructor
import pl.jwizard.api.domain.guild.AudioPlayer
import pl.jwizard.api.domain.guild.Inactivity
import pl.jwizard.api.domain.guild.Voting

@NoArgConstructor
data class StandaloneGuildDetailsResDto(
	val legacyPrefix: String,
	val djRoleName: String,
	val locale: String,
	val slashEnabled: Boolean,
	val enabledCommands: List<String>,
	val enabledSlashCommands: List<String>,
	val inactivity: Inactivity,
	val voting: Voting,
	val audioPlayer: AudioPlayer,
	val enabledModules: List<String>,
	val musicTextChannelId: String?,
)
