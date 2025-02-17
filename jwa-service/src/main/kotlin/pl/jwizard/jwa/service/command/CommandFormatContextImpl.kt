package pl.jwizard.jwa.service.command

import pl.jwizard.jwl.command.CommandFormatContext

class CommandFormatContextImpl(
	override val prefix: String,
	override val isSlashEvent: Boolean,
) : CommandFormatContext
