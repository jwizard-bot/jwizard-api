/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.command

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwizard.commands")
data class CommandProperties(
	var others: List<Command> = emptyList(),
	var music: List<Command> = emptyList(),
	var playlist: List<Command> = emptyList(),
	var dj: List<Command> = emptyList(),
	var vote: List<Command> = emptyList(),
	var settings: List<Command> = emptyList(),
)

data class Command(
	var name: String,
	var aliases: String,
	var arguments: List<CommandArgument> = emptyList(),
)

data class CommandArgument(
	var argName: String,
	var castedType: String,
	var required: Boolean,
)
