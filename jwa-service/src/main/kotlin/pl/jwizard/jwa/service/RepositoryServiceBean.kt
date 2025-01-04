/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacadeBean
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.rest.repository.dto.LastUpdateDto
import pl.jwizard.jwa.rest.repository.dto.PrimaryLanguageDto
import pl.jwizard.jwa.rest.repository.dto.RepositoryResDto
import pl.jwizard.jwa.rest.repository.spi.RepositoryService
import pl.jwizard.jwa.service.spi.BuildInfoSupplier
import pl.jwizard.jwa.service.spi.dto.ProjectVersionRow
import pl.jwizard.jwl.http.HttpClientFacadeBean
import pl.jwizard.jwl.i18n.I18nBean
import pl.jwizard.jwl.i18n.source.I18nLibDynamicSource
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.util.ext.getAsText
import pl.jwizard.jwl.vcs.VcsConfigBean
import pl.jwizard.jwl.vcs.VcsRepository

/**
 * Service class responsible for managing repository-related data.
 *
 * This service fetches repository information, such as repository details, build information, and primary programming
 * language, and caches the results for performance optimization.
 *
 * @property vcsConfig The configuration for version control system (VCS) operations.
 * @property cacheFacade The cache facade used to cache repository-related data.
 * @property i18n The internationalization (i18n) service used to retrieve localized data.
 * @property httpClientFacade The HTTP client used to fetch repository and language color data.
 * @property environment The environment properties containing configuration values.
 * @property buildInfoSupplier The service used to fetch build-related information for projects.
 * @author Miłosz Gilga
 */
@SingletonService
class RepositoryServiceBean(
	private val vcsConfig: VcsConfigBean,
	private val cacheFacade: CacheFacadeBean,
	private val i18n: I18nBean,
	private val httpClientFacade: HttpClientFacadeBean,
	private val environment: EnvironmentBean,
	private val buildInfoSupplier: BuildInfoSupplier,
) : RepositoryService {

	private val githubApiUrl = environment.getProperty<String>(ServerProperty.GITHUB_API_URL)
	private val organizationName = environment.getProperty<String>(AppBaseProperty.VCS_ORGANIZATION_NAME)
	private val githubLanguageColorApiUrl = environment.getProperty<String>(ServerProperty.GITHUB_LANGUAGE_COLOR_API_URL)

	/**
	 * Retrieves all repositories with the corresponding details, such as build information, primary language, last
	 * update date, and description.
	 *
	 * This method checks the cache for repository data. If the data is not available in the cache, it computes the
	 * repository details by calling the GitHub API and fetching the corresponding build information, then caches the
	 * result for future requests.
	 *
	 * @param language The language code used for localized repository names and descriptions.
	 * @return A list of [RepositoryResDto] objects containing the repository details.
	 */
	override fun getAllRepositories(language: String?): List<RepositoryResDto> {
		val projectsBuildInfo = buildInfoSupplier.fetchProjectsBuildInfo()
			.associateBy { it.name }

		val cachedRepositories = cacheFacade.getCachedList(
			cacheEntity = CacheEntity.REPOSITORIES,
			key = 0,
			computeOnAbsent = { fetchRepositoryLanguageInfo(projectsBuildInfo, language) },
			revalidateData = { revalidateCache(projectsBuildInfo, it) },
		)
		return cachedRepositories.map {
			it.copy(
				name = i18n.tRaw(I18nLibDynamicSource.PROJECT_NAME, arrayOf(it.slug), language),
				description = i18n.tRaw(I18nLibDynamicSource.PROJECT_DESCRIPTION, arrayOf(it.slug), language),
			)
		}
	}

	/**
	 * Fetches the repository details including the primary language, description, license, and other relevant
	 * information. It filters the repositories based on the declared repositories and ensures that the project build
	 * info is available.
	 *
	 * @param projectsBuildInfo A map of project names to their build information.
	 * @param language The language code used for localized repository names and descriptions.
	 * @return A list of [RepositoryResDto] objects with detailed repository information.
	 */
	private fun fetchRepositoryLanguageInfo(
		projectsBuildInfo: Map<String, ProjectVersionRow>,
		language: String?,
	): List<RepositoryResDto> {
		val declaredRepos = VcsRepository.entries.map { environment.getProperty<String>(it.property) }
		val repositories = httpClientFacade.getJsonListCall("$githubApiUrl/orgs/$organizationName/repos")
		val colors = httpClientFacade.getJsonListCall(githubLanguageColorApiUrl)

		val declaredRepositories = repositories.filter {
			val name = it.getAsText("name")
			declaredRepos.contains(name) && projectsBuildInfo.containsKey(name)
		}
		val parsedRepositories = mutableListOf<RepositoryResDto>()

		for (repo in declaredRepositories) {
			val (name, latestVersionLong, lastUpdatedUtc) = projectsBuildInfo[repo.getAsText("name")]!!
			if (latestVersionLong == null || lastUpdatedUtc == null) {
				continue
			}
			val (shortSha, link) = vcsConfig.createSafeSnapshotUrl(name, latestVersionLong)
			val programmingLanguage = repo.getAsText("language")
			val color = if (colors.has(programmingLanguage)) {
				colors.get(programmingLanguage).getAsText("color")
			} else {
				null
			}
			val repositoryRes = RepositoryResDto(
				name = i18n.tRaw(I18nLibDynamicSource.PROJECT_NAME, arrayOf(name), language),
				slug = name,
				description = i18n.tRaw(I18nLibDynamicSource.PROJECT_DESCRIPTION, arrayOf(name), language),
				link = repo.getAsText("html_url"),
				primaryLanguage = PrimaryLanguageDto(
					name = repo.getAsText("language"),
					color = color,
				),
				lastUpdate = LastUpdateDto(
					buildSha = shortSha,
					buildDate = lastUpdatedUtc,
					link = link
				),
			)
			parsedRepositories.add(repositoryRes)
		}
		return parsedRepositories
	}

	/**
	 * Revalidates the cached repository data by comparing the build information. This ensures that the cached data is
	 * still up-to-date and corresponds to the latest build info.
	 *
	 * @param fetchedBuildInfo A map of the latest build info for each project.
	 * @param persistedBuildInfo The list of repositories already persisted in the cache.
	 * @return A boolean indicating whether the cached data is still valid.
	 */
	private fun revalidateCache(
		fetchedBuildInfo: Map<String, ProjectVersionRow>,
		persistedBuildInfo: List<RepositoryResDto>,
	) = persistedBuildInfo.all {
		val buildInfo = fetchedBuildInfo[it.slug] ?: return false
		val shortSha = if (buildInfo.latestVersionLong != null) {
			vcsConfig.createShortSha(buildInfo.latestVersionLong)
		} else {
			null
		}
		shortSha == it.lastUpdate.buildSha
	}
}
