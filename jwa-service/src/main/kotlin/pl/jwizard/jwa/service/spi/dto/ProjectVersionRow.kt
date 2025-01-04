/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service.spi.dto

import java.time.LocalDateTime

/**
 * Data class representing a project version with its metadata.
 *
 * This class stores the details of a specific project version, including its name, the latest version as a string (if
 * available), and the timestamp of the last update in UTC.
 *
 * @property name The name of the project version.
 * @property latestVersionLong The latest version number of the project, represented as a string. Can be null if not
 *           available.
 * @property lastUpdatedUtc The UTC timestamp when the project version was last updated. Can be null if not available.
 * @author Miłosz Gilga
 */
data class ProjectVersionRow(
	val name: String,
	val latestVersionLong: String?,
	val lastUpdatedUtc: LocalDateTime?,
)
