/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.documentation.spi

import pl.jwizard.jwa.rest.route.documentation.dto.DocumentationResDto

/**
 * Service interface for managing and retrieving documentation resources.
 *
 * This interface defines the contract for accessing documentation-related data, enabling the implementation of methods
 * to fetch and process documentation details based on specific criteria, such as language preferences.
 *
 * @author Miłosz Gilga
 */
interface DocumentationService {

	/**
	 * Retrieves a list of all available documentation resources, optionally filtered by language.
	 *
	 * @param language An optional parameter specifying the language code.
	 * @return A list of [DocumentationResDto] objects representing the available documentation resources.
	 */
	fun getAllDocumentations(language: String?): List<DocumentationResDto>
}
