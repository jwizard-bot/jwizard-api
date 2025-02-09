/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.dto

/**
 * A generic data transfer object (DTO) for representing a collection of options along with a default option.
 *
 * This can be used in REST API responses to provide pre-configured sets of selectable options.
 *
 * @param T The type of the options.
 * @property defaultOption The default option that should be pre-selected or highlighted.
 * @property options A list of available options of type T.
 * @author Miłosz Gilga
 */
data class OptionsResDto<T>(
	val defaultOption: T,
	val options: List<T>,
)
