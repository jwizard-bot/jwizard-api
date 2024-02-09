/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.command

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.jwizard.api.network.command.dto.CommandsResDto

@RestController
@RequestMapping("/api/v1/command")
class CommandController(
	private val commandService: CommandService
) {
	@GetMapping("/all")
	fun getAllCommands(): ResponseEntity<Map<String, CommandsResDto>> {
		return ResponseEntity.ok(commandService.getCommandsBaseLang())
	}
}
