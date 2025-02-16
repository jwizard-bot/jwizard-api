package pl.jwizard.jwa.service.spi.dto

import java.time.LocalDateTime

data class ProjectVersionRow(
	val name: String,
	val latestVersionLong: String?,
	val lastUpdatedUtc: LocalDateTime?,
)
