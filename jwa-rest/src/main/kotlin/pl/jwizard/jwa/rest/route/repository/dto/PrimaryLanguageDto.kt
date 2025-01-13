/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.repository.dto

import java.io.Serializable

/**
 * Data transfer object (DTO) representing the primary programming language of a repository.
 *
 * This class contains the name of the primary language used in the repository along with an optional color
 * representation for that language (typically used for visual display in user interfaces).
 *
 * @property name The name of the primary programming language (e.g., "Kotlin", "Java", "Python").
 * @property color An optional color code associated with the primary language (e.g., hex color code for UI display).
 * @author Miłosz Gilga
 */
data class PrimaryLanguageDto(
	val name: String,
	val color: String?,
) : Serializable
