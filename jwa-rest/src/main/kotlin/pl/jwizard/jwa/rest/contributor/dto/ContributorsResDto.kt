/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.contributor.dto

/**
 * A data transfer object (DTO) that represents the response containing information about contributors and their
 * associated variants.
 *
 * @property contributors A list of [ProjectContributor] objects representing the contributors.
 * @property variants A list of variant names available for the project.
 * @property initVariant An optional initial variant that may be used to indicate a default or pre-selected variant.
 * @author Miłosz Gilga
 */
data class ContributorsResDto(
	val contributors: List<ProjectContributor>,
	val variants: List<String>,
	val initVariant: String?,
)
