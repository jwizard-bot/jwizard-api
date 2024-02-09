/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.domain.guild

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import pl.jwizard.api.config.annotation.NoArgConstructor
import pl.jwizard.api.network.guild.AudioPlayerProperties
import pl.jwizard.api.network.guild.InactivityProperties
import pl.jwizard.api.network.guild.VotingProperties

@NoArgConstructor
@Document(collection = "guilds")
data class GuildDocument(
	@Id
	val id: ObjectId = ObjectId(),

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

	@field:Field(write = Field.Write.ALWAYS)
	val musicTextChannelId: String?,

	val guildId: String,
)

@NoArgConstructor
data class Inactivity(
	val leaveEmptyChannelSec: Long,
	val leaveNoTracksChannelSec: Long,
) {
	constructor(props: InactivityProperties) : this(
		props.leaveEmptyChannelSecAll.defined,
		props.leaveNoTracksChannelSecAll.defined
	)
}

@NoArgConstructor
data class Voting(
	val timeToFinishSec: Long,
	val percentageRatio: Short,
) {
	constructor(props: VotingProperties) : this(
		props.timeToFinishSecAll.defined,
		props.percentageRatio
	)
}

@NoArgConstructor
data class AudioPlayer(
	val timeAfterAutoChooseSec: Long,
	val randomAutoChoose: Boolean,
	val tracksNumberChoose: Long,
	val maxRepeatsOfTrack: Long,
	val defaultVolume: Long,
) {
	constructor(props: AudioPlayerProperties) : this(
		props.timeAfterAutoChooseSecAll.defined,
		props.randomAutoChoose,
		props.tracksNumberChooseAll.defined,
		props.maxRepeatsOfTrackAll.defined,
		props.defaultVolumeAll.defined,
	)
}
