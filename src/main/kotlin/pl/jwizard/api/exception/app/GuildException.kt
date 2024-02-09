/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.exception.app

import org.springframework.http.HttpStatus
import pl.jwizard.api.exception.AbstractRestException
import pl.jwizard.api.i18n.set.ApiLocaleSet

object GuildException {
	class GuildNotExistException(guildId: String) : AbstractRestException(
		httpStatus = HttpStatus.NOT_FOUND,
		placeholder = ApiLocaleSet.EXC_GUILD_NOT_EXIST,
		clazz = GuildNotExistException::class,
		logMessage = "Attempt to perform operation on non existing guild with ID: $guildId"
	)
}
