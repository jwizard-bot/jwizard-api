/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.status.dto

/**
 * Data class representing the global status response.
 *
 * @property operational A boolean flag indicating if the system is operational or not. If `true`, the system is
 *           operational; if `false`, there are issues, if `null` could not get system status.
 * @property description a textual description providing additional details about the system's status.
 * @property sourceUrl a URL pointing to the source or documentation that provides more information about the system's
 *           status.
 * @author Miłosz Gilga
 */
data class GlobalStatusResDto(
	val operational: Boolean?,
	val description: String,
	val sourceUrl: String,
)
