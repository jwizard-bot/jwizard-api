package pl.jwizard.jwa.gateway.http.rest.route.packages.dto

import java.io.Serializable

data class PackageRowResDto(
	val name: String,
	val link: String,
) : Serializable
