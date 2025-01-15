/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service.dto

/**
 * Data class representing the data of a bot instance.
 *
 * This contains information about a specific bot instance, including its application ID and associated color.
 *
 * @property appId The unique application identifier for the bot instance.
 * @property color The color associated with the bot instance, represented as a string (ex. a hex color code).
 * @author Miłosz Gilga
 */
data class BotInstanceData(
	val appId: String,
	val color: String,
)
