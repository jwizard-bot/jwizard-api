/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.rest.route.documentation.dto.DocumentationLinkDto
import pl.jwizard.jwa.rest.route.documentation.dto.DocumentationResDto
import pl.jwizard.jwa.rest.route.documentation.spi.DocumentationService
import pl.jwizard.jwl.i18n.I18nBean
import pl.jwizard.jwl.i18n.source.I18nLibDynamicSource
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.vcs.documented.DocumentedRepository

/**
 * Service implementation for managing documentation resources.
 *
 * This class fetches and processes documentation-related data using internationalization (i18n) support and
 * environment properties. It transforms the raw repository data into structured DTOs that can be returned to the client.
 *
 * @property i18n The [I18nBean] instance for handling internationalization and localization of texts.
 * @property environment The [EnvironmentBean] instance for accessing application environment properties.
 * @author Miłosz Gilga
 */
@SingletonService
class DocumentationServiceBean(
	private val i18n: I18nBean,
	private val environment: EnvironmentBean,
) : DocumentationService {

	/**
	 * The base URL for documentation links, loaded from application properties.
	 */
	private val docsUrl = environment.getProperty<String>(AppBaseProperty.SERVICE_DOCS_URL)

	/**
	 * Retrieves all available documentation resources.
	 *
	 * This method processes the data from [DocumentedRepository] entries, applies localization using the provided
	 * language parameter, and constructs a list of [DocumentationResDto] objects. Each DTO includes a localized name,
	 * description, and a list of associated documentation links.
	 *
	 * @param language The optional language code (ex. "en", "pl").
	 * @return A list of [DocumentationResDto] objects representing the documentation resources.
	 */
	override fun getAllDocumentations(language: String?) = DocumentedRepository.entries.map {
		val repoName = environment.getProperty<String>(it.repository.property)
		DocumentationResDto(
			name = i18n.tRaw(I18nLibDynamicSource.PROJECT_NAME, arrayOf(repoName), language),
			description = i18n.tRaw(I18nLibDynamicSource.PROJECT_DESCRIPTION, arrayOf(repoName), language),
			types = it.types.map { type ->
				DocumentationLinkDto(
					name = i18n.t(type, language),
					link = "$docsUrl/${it.slug}/${type.typeName}",
				)
			}
		)
	}
}
