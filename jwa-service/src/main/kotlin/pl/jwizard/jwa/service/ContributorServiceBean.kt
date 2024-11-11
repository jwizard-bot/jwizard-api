/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.HttpClientFacadeBean
import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacadeBean
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.core.util.ext.save
import pl.jwizard.jwa.core.util.ext.take
import pl.jwizard.jwa.rest.contributor.dto.ContributorsResDto
import pl.jwizard.jwa.rest.contributor.dto.ProjectContributor
import pl.jwizard.jwa.rest.contributor.spi.ContributorService
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.vcs.VcsRepository

/**
 * Service class responsible for retrieving and caching contributors' data for a project.
 *
 * This service retrieves contributors from GitHub repositories, combines the information for each contributor, and
 * caches the result to avoid repeated external API calls.
 *
 * @property environmentBean The environment properties containing configuration values, such as API URLs.
 * @property cacheFacadeBean The cache facade used to cache the list of contributors.
 * @property httpClientFacadeBean The HTTP client used for making requests to the GitHub API to fetch contributor data.
 * @author Miłosz Gilga
 */
@SingletonService
class ContributorServiceBean(
	private val environmentBean: EnvironmentBean,
	private val cacheFacadeBean: CacheFacadeBean,
	private val httpClientFacadeBean: HttpClientFacadeBean,
) : ContributorService {

	companion object {
		/**
		 * The default variant value for contributors, indicating all repositories.
		 */
		private const val DEFAULT_VARIANT = "all"
	}

	private val githubApiUrl = environmentBean.getProperty<String>(ServerProperty.GITHUB_API_URL)
	private val organizationName = environmentBean.getProperty<String>(AppBaseProperty.VCS_ORGANIZATION_NAME)

	/**
	 * A list of project repositories that are "standalone".
	 */
	private val projectRepositories = VcsRepository.entries.filter(VcsRepository::standalone)

	/**
	 * A list of variants for the repositories based on configuration.
	 */
	private val variants = projectRepositories.map { environmentBean.getProperty<String>(it.property) }

	/**
	 * Retrieves the project contributors, either from the cache or by computing the list if absent.
	 *
	 * The method checks the cache for contributors data and returns it if available. If not, it computes the list by
	 * making API calls and caches the result for subsequent calls.
	 *
	 * @return A [ContributorsResDto] object containing a list of contributors and repository variants.
	 */
	override fun getProjectContributors(): ContributorsResDto {
		val repositoryVariants = variants.toMutableList()
		repositoryVariants.add(0, DEFAULT_VARIANT)

		val contributors = cacheFacadeBean.getCachedList(CacheEntity.CONTRIBUTORS, 0, ::computeOnAbsent)
		return ContributorsResDto(
			contributors = contributors,
			variants = repositoryVariants,
			initVariant = DEFAULT_VARIANT,
		)
	}

	/**
	 * Computes the list of project contributors by aggregating data from each repository. This method fetches
	 * contributors for each project and processes the data to group them by their repositories.
	 *
	 * Each contributor's variant list is also checked to see if they contribute to all repositories, in which case
	 * the "all" variant is added to their list.
	 *
	 * @return A list of [ProjectContributor] objects, representing each contributor with their associated variants.
	 */
	private fun computeOnAbsent(): List<ProjectContributor> {
		val contributors = projectRepositories
			.map { getPerProjectContributorsList(it) }
			.flatten()

		val contributorsWithVariants = contributors.map { contributor ->
			val contributorVariants = contributors
				.filter { it.take("login") == contributor.take("login") }
				.map { it.take("repository") }
				.toMutableList()

			val memberContributeAllProjects = contributorVariants.containsAll(variants)
			if (memberContributeAllProjects) {
				contributorVariants.add(0, DEFAULT_VARIANT)
			}
			ProjectContributor(
				nickname = contributor.take("login"),
				profileLink = contributor.take("html_url"),
				profileImageUrl = contributor.take("avatar_url"),
				variants = contributorVariants
			)
		}
		return contributorsWithVariants.distinctBy(ProjectContributor::nickname)
	}

	/**
	 * Retrieves the list of contributors for a specific project repository by calling the GitHub API. This method makes
	 * a GET request to the GitHub API to fetch the contributors for a given repository and filters the results to
	 * include only user-type contributors.
	 *
	 * The repository name is dynamically obtained from the environment configuration based on the repository type.
	 *
	 * @param repository The repository for which contributors are to be fetched.
	 * @return A list of maps containing contributor data retrieved from the GitHub API.
	 */
	private fun getPerProjectContributorsList(repository: VcsRepository): List<Map<String, Any>> {
		val repositoryName = environmentBean.getProperty<String>(repository.property)
		val response = httpClientFacadeBean.getJsonListCall(
			url = "$githubApiUrl/repos/$organizationName/$repositoryName/contributors"
		)
		val outputWithOnlyMembers = response.filter { it.take("type").lowercase() == "user" }
		for (member in outputWithOnlyMembers) {
			member.save("repository", environmentBean.getProperty(repository.property))
		}
		return outputWithOnlyMembers
	}
}
