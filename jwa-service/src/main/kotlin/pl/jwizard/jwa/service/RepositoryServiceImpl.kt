package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacadeBean
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.rest.route.repository.dto.LastUpdateDto
import pl.jwizard.jwa.rest.route.repository.dto.PrimaryLanguageDto
import pl.jwizard.jwa.rest.route.repository.dto.RepositoryResDto
import pl.jwizard.jwa.rest.route.repository.spi.RepositoryService
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

@SingletonService
internal class RepositoryServiceBean(
	private val vcsConfig: VcsConfigBean,
	private val cacheFacade: CacheFacadeBean,
	private val i18n: I18nBean,
	private val httpClientFacade: HttpClientFacadeBean,
	private val environment: EnvironmentBean,
	private val buildInfoSupplier: BuildInfoSupplier,
) : RepositoryService {

	private val githubApiUrl = environment
		.getProperty<String>(ServerProperty.GITHUB_API_URL)

	private val organizationName = environment
		.getProperty<String>(AppBaseProperty.VCS_ORGANIZATION_NAME)

	private val githubLanguageColorApiUrl = environment
		.getProperty<String>(ServerProperty.GITHUB_LANGUAGE_COLOR_API_URL)

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
				name = i18n.tRaw(I18nAppFragmentSource.PROJECT_NAME, arrayOf(it.slug), language),
				description = i18n.tRaw(
					I18nAppFragmentSource.PROJECT_DESCRIPTION,
					arrayOf(it.slug),
					language
				),
			)
		}
	}

	private fun fetchRepositoryLanguageInfo(
		projectsBuildInfo: Map<String, ProjectVersionRow>,
		language: String?,
	): List<RepositoryResDto> {
		val declaredRepos = VcsRepository.entries.map { environment.getProperty<String>(it.property) }
		val repositories = httpClientFacade
			.getJsonListCall("$githubApiUrl/orgs/$organizationName/repos")
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
				name = i18n.tRaw(I18nAppFragmentSource.PROJECT_NAME, arrayOf(name), language),
				slug = name,
				description = i18n.tRaw(I18nAppFragmentSource.PROJECT_DESCRIPTION, arrayOf(name), language),
				link = repositoryUrl,
				primaryLanguage = PrimaryLanguageDto(
					name = repo.getAsText("language"),
					color = color,
				),
				lastUpdate = LastUpdateDto(
					buildSha = createShortSha(latestVersionLong),
					buildDate = lastUpdatedUtc,
					link = "$repositoryUrl/tree/$latestVersionLong"
				),
			)
			parsedRepositories.add(repositoryRes)
		}
		return parsedRepositories
	}

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
