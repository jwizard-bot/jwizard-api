/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.repository.dto

import java.io.Serializable
import java.time.LocalDateTime

/**
 * Data transfer object (DTO) representing the last update information of a repository or project.
 *
 * This class encapsulates information related to the most recent build, including the SHA of the build, the date of
 * the build, and a link to the associated resource or details about the build.
 *
 * @property buildSha The SHA hash representing the specific build version.
 * @property buildDate The date and time when the build was created.
 * @property link A URL link providing more information about the build or the associated repository.
 * @author Miłosz Gilga
 */
data class LastUpdateDto(
	val buildSha: String,
	val buildDate: LocalDateTime,
	val link: String,
) : Serializable
