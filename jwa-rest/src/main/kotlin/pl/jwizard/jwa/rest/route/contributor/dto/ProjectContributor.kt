/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.contributor.dto

import java.io.Serializable

/**
 * A data transfer object (DTO) that represents a project contributor with associated details. This class is used to
 * store and transfer information about individual contributors within a project.
 *
 * @property nickname The contributor's nickname or username associated with the project.
 * @property profileLink A URL linking to the contributor's profile or page on the platform.
 * @property profileImageUrl A URL pointing to the contributor's profile image.
 * @property variants A list of variants associated with the contributor, indicating specific roles or configurations.
 * @author Miłosz Gilga
 */
data class ProjectContributor(
	val nickname: String,
	val profileLink: String,
	val profileImageUrl: String,
	val variants: List<String>,
) : Serializable
