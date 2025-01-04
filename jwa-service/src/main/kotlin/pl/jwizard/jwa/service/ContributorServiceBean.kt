/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import com.fasterxml.jackson.databind.JsonNode
import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacadeBean
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.rest.contributor.dto.ContributorsResDto
import pl.jwizard.jwa.rest.contributor.dto.ProjectContributor
import pl.jwizard.jwa.rest.contributor.dto.ProjectVariant
import pl.jwizard.jwa.rest.contributor.spi.ContributorService
import pl.jwizard.jwl.http.HttpClientFacadeBean
import pl.jwizard.jwl.i18n.I18nBean
import pl.jwizard.jwl.i18n.source.I18nLibDynamicSource
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.util.ext.getAsText
import pl.jwizard.jwl.vcs.VcsConfigBean
import pl.jwizard.jwl.vcs.VcsRepository

/**
 * Service class responsible for retrieving and caching contributors' data for a project.
 *
 * This service retrieves contributors from GitHub repositories, combines the information for each contributor, and
 * caches the result to avoid repeated external API calls.
 *
 * @property environment The environment properties containing configuration values, such as API URLs.
 * @property cacheFacade The cache facade used to cache the list of contributors.
 * @property httpClientFacade The HTTP client used for making requests to the GitHub API to fetch contributor data.
 * @property i18n A bean responsible for providing localized translations for the status description.
 * @property vcsConfig A configuration bean for managing VCS repository details, such as repository names.
 * @author Miłosz Gilga
 */
@SingletonService
class ContributorServiceBean(
	private val environment: EnvironmentBean,
	private val cacheFacade: CacheFacadeBean,
	private val httpClientFacade: HttpClientFacadeBean,
	private val i18n: I18nBean,
	private val vcsConfig: VcsConfigBean,
) : ContributorService {

	private val githubApiUrl = environment.getProperty<String>(ServerProperty.GITHUB_API_URL)
	private val organizationName = environment.getProperty<String>(AppBaseProperty.VCS_ORGANIZATION_NAME)

	/**
	 * A list of variants for the repositories based on configuration.
	 */
	private val variants = VcsRepository.entries.map { environment.getProperty<String>(it.property) }

	/**
	 * The default variant value for contributors, indicating all repositories.
	 */
	private val defaultVariant = vcsConfig.getRepositoryName(VcsRepository.ALL)

	/**
	 * Retrieves the project contributors, either from the cache or by computing the list if absent.
	 *
	 * The method checks the cache for contributors data and returns it if available. If not, it computes the list by
	 * making API calls and caches the result for subsequent calls.
	 *
	 * @param language The language code used to retrieve localized project names.
	 * @return A [ContributorsResDto] object containing a list of contributors and repository variants.
	 */
	override fun getProjectContributors(language: String?): ContributorsResDto {
		val repositoryVariants = VcsRepository.entries
			.associate {
				val repoName = vcsConfig.getRepositoryName(it)
				repoName to ProjectVariant(
					name = i18n.tRaw(I18nLibDynamicSource.PROJECT_NAME, arrayOf(repoName), language),
					position = it.position,
				)
			}
			.toMutableMap()

		val contributors = cacheFacade.getCachedList(CacheEntity.CONTRIBUTORS, 0, ::computeOnAbsent)
		return ContributorsResDto(
			contributors = contributors,
			variants = repositoryVariants.toMap(),
			initVariant = defaultVariant,
		)
	}

	/**
	 * Computes the list of project contributors by retrieving data from GitHub and categorizing by repository variants.
	 *
	 * This method is called when the contributors' data is absent in the cache. It iterates over each standalone
	 * repository, retrieves its contributors from GitHub, and then consolidates each contributor's repository variants.
	 * Contributors who have contributions across all repositories are assigned a default variant.
	 *
	 * @return A list of [ProjectContributor] objects, each containing the contributor's details and a list of variants.
	 */
	private fun computeOnAbsent(): List<ProjectContributor> {
		val contributors = mutableListOf<Pair<String, JsonNode>>()
		for (repository in VcsRepository.entries.filter { it != VcsRepository.ALL }) {
			val repositoryContributors = getPerProjectContributors(repository)
			for (contributor in repositoryContributors) {
				contributors += Pair(environment.getProperty(repository.property), contributor)
			}
		}
		val contributorsWithVariants = contributors.map { (_, contributor) ->
			val contributorVariants = contributors
				.filter { (_, user) -> user.getAsText("login") == contributor.getAsText("login") }
				.map { (repository, _) -> repository }
				.toMutableList()

			val memberContributeAllProjects = contributorVariants.containsAll(variants)
			if (memberContributeAllProjects) {
				contributorVariants.add(0, defaultVariant)
			}
			ProjectContributor(
				nickname = contributor.getAsText("login"),
				profileLink = contributor.getAsText("html_url"),
				profileImageUrl = contributor.getAsText("avatar_url"),
				variants = contributorVariants,
			)
		}
		return contributorsWithVariants.distinctBy(ProjectContributor::nickname)
	}

	/**
	 * Retrieves the list of contributors for a specific project repository from GitHub.
	 *
	 * This method constructs an API request to fetch the contributors for a given repository from GitHub's API, filtered
	 * to include only users (excluding bots or other types).
	 *
	 * @param repository The [VcsRepository] for which contributors should be retrieved.
	 * @return A mutable list of [JsonNode] objects, each representing a contributor.
	 */
	private fun getPerProjectContributors(repository: VcsRepository): MutableList<JsonNode> {
		val repositoryName = environment.getProperty<String>(repository.property)
		val response = httpClientFacade.getJsonListCall(
			url = "$githubApiUrl/repos/$organizationName/$repositoryName/contributors"
		)
		return response.filter { it.getAsText("type") == "User" }.toMutableList()
	}
}
