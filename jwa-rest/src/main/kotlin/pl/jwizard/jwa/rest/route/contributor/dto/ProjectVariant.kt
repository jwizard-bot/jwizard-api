/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.contributor.dto

/**
 * Represents a variant of a project with a name and position.
 *
 * @property name The name of the project variant.
 * @property position The position of the project variant, typically used for sorting or prioritization.
 * @author Miłosz Gilga
 */
data class ProjectVariant(
	val name: String,
	val position: Int,
)
