/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.guild

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwizard.guild.defaults")
data class GuildDefaultProperties(
	var inactivity: InactivityProperties = InactivityProperties(),
	var voting: VotingProperties = VotingProperties(),
	var audioPlayer: AudioPlayerProperties = AudioPlayerProperties(),
)

data class InactivityProperties(
	var leaveEmptyChannelSecAll: DefinedWithMax = DefinedWithMax(10, 30, 60),
	var leaveNoTracksChannelSecAll: DefinedWithMax = DefinedWithMax(10, 30, 60),
)

data class VotingProperties(
	var percentageRatio: Short = 50,
	var timeToFinishSecAll: DefinedWithMax = DefinedWithMax(10, 30, 600),
)

data class AudioPlayerProperties(
	var randomAutoChoose: Boolean = true,
	var tracksNumberChooseAll: DefinedWithMax = DefinedWithMax(2, 10, 20),
	var timeAfterAutoChooseSecAll: DefinedWithMax = DefinedWithMax(10, 20, 120),
	var maxRepeatsOfTrackAll: DefinedWithMax = DefinedWithMax(2, 30, 100),
	var defaultVolumeAll: DefinedWithMax = DefinedWithMax(5, 100, 150),
)

data class DefinedWithMax(
	var min: Long,
	var defined: Long,
	var max: Long,
)
