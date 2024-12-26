/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.packages.dto

import java.io.Serializable

/**
 * Data transfer object representing a row of package data.
 *
 * This class is used to transfer package data, including the package name and a related link, across different layers
 * of the application.
 *
 * @property name The name of the package.
 * @property link The link associated with the package.
 * @author Miłosz Gilga
 */
data class PackageRowResDto(
	val name: String,
	val link: String,
) : Serializable
