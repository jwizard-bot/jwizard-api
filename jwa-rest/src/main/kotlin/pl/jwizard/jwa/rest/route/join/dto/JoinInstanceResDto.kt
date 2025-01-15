/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.join.dto

/**
 * Data class representing a response for a join instance.
 *
 * This contains the details of a specific joinable instance, including its name, color, and a link to the instance.
 *
 * @property name The name of the join instance.
 * @property color The color associated with the join instance, represented as a string (e.g., a hex color code).
 * @property link A URL link to the join instance, typically used to navigate or join the instance.
 * @author Miłosz Gilga
 */
data class JoinInstanceResDto(
	val name: String,
	val color: String,
	val link: String,
)
