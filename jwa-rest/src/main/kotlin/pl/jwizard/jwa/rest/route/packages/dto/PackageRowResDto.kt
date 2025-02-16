package pl.jwizard.jwa.rest.route.packages.dto

import java.io.Serializable

data class PackageRowResDto(
	val name: String,
	val link: String,
) : Serializable
