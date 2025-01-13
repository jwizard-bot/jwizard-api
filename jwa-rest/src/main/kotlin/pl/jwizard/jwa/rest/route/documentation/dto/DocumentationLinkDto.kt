/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.documentation.dto

/**
 * Data Transfer Object (DTO) representing a link to documentation.
 *
 * This class is used to encapsulate information about a specific documentation resource, including its name and the
 * URL where it can be accessed.
 *
 * @property name The name or title of the documentation resource.
 * @property link The URL (link) pointing to the documentation resource.
 * @author Miłosz Gilga
 */
data class DocumentationLinkDto(
	val name: String,
	val link: String,
)
