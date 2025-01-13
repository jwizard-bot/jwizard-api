/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.home.dto

/**
 * Data Transfer Object (DTO) representing feature details in the system.
 *
 * @property name The name of the feature.
 * @property description A brief description of the feature.
 * @property isActive Indicates whether the feature is active (`true`) or inactive (`false`).
 * @author Miłosz Gilga
 */
data class FeaturesResDto(
	val name: String,
	val description: String,
	val isActive: Boolean,
)
