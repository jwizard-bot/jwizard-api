/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.contributor

import pl.jwizard.api.github.GithubApiProperties

enum class ContributeVariant(
	val variant: String,
	private val repository: String?,
) {
	ALL("all", null),
	CORE("core", "jwizard-core"),
	API("api", "jwizard-api"),
	FRONT("front", "jwizard-web"),
	;

	companion object {
		fun getAssignableVariants(): List<ContributeVariant> = entries.filter { it.repository != null }

		fun getNamesFromVariants(variants: List<ContributeVariant>): List<String> = variants.map { it.variant }

		fun getNamesFromVariants(): List<String> = getNamesFromVariants(entries)
	}

	fun getUrl(properties: GithubApiProperties): String {
		val (apiUrl, organizationName) = properties
		return "$apiUrl/repos/$organizationName/${repository}/contributors"
	}
}
