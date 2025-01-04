/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.repository.dto

import java.io.Serializable

/**
 * Data transfer object (DTO) representing the details of a repository.
 *
 * This class contains information about a repository, including its name, description, link, primary language, last
 * update details, and license information.
 *
 * @property name The name of the repository.
 * @property slug The slug of the repository (without spaces).
 * @property description A short description of the repository.
 * @property link The URL link to the repository's page (ex. on GitHub).
 * @property primaryLanguage The primary language used in the repository, represented as a [PrimaryLanguageDto].
 * @property lastUpdate The details of the last update to the repository, represented as a [LastUpdateDto].
 * @author Miłosz Gilga
 */
data class RepositoryResDto(
	val name: String,
	val slug: String,
	val description: String,
	val link: String,
	val primaryLanguage: PrimaryLanguageDto,
	val lastUpdate: LastUpdateDto,
) : Serializable
