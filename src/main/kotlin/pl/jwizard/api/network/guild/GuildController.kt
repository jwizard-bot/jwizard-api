/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.guild

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.jwizard.api.network.guild.dto.StandaloneGuildDetailsResDto

@RestController
@RequestMapping("/api/v1/guild")
class GuildController(
	private val guildService: GuildService,
) {
	@PostMapping("/standalone/settings/{guildId}")
	fun createOrGetGuildSettings(
		@PathVariable guildId: String,
	): ResponseEntity<StandaloneGuildDetailsResDto> = ResponseEntity.ok(guildService.createOrGetGuildSettings(guildId))

	@DeleteMapping("/standalone/settings/music-channel/guild/{guildId}")
	fun deletePersistedMusicChannel(
		@PathVariable guildId: String,
	): ResponseEntity<Unit> {
		guildService.deletePersistedMusicChannel(guildId)
		return ResponseEntity(HttpStatus.NO_CONTENT)
	}

	@DeleteMapping("/standalone/settings/{guildId}")
	fun deleteGuildSettings(
		@PathVariable guildId: String,
	): ResponseEntity<Unit> {
		guildService.deleteGuildSettings(guildId)
		return ResponseEntity(HttpStatus.NO_CONTENT)
	}
}
