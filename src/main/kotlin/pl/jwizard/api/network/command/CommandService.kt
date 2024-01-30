/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.command

import pl.jwizard.api.network.command.dto.CommandsResDto

interface CommandService {
	fun getCommandsBaseLang(): CommandsResDto
}
