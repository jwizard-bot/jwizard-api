/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.documentation.dto

/**
 * Data Transfer Object (DTO) representing a documentation resource.
 *
 * This class is used to encapsulate detailed information about a specific documentation resource, including its name,
 * description, and associated types of documentation links.
 *
 * @property name The name or title of the documentation resource.
 * @property description A detailed description of the documentation resource, providing context or additional information.
 * @property types A list of [DocumentationLinkDto] objects representing associated links or references related to the
 *           documentation resource.
 * @author Miłosz Gilga
 */
data class DocumentationResDto(
	val name: String,
	val description: String,
	val types: List<DocumentationLinkDto>,
)
