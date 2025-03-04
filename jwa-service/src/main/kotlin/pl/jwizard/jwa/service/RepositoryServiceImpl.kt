package pl.jwizard.jwa.service

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacade
import pl.jwizard.jwa.core.i18n.I18nAppFragmentSource
import pl.jwizard.jwa.http.rest.route.repository.RepositoryService
import pl.jwizard.jwa.http.rest.route.repository.dto.LastUpdateDto
import pl.jwizard.jwa.http.rest.route.repository.dto.PrimaryLanguageDto
import pl.jwizard.jwa.http.rest.route.repository.dto.RepositoryResDto
import pl.jwizard.jwa.service.spi.BuildInfoSupplier
import pl.jwizard.jwa.service.spi.dto.ProjectVersionRow
import pl.jwizard.jwl.i18n.I18n
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.property.VcsProperty
import pl.jwizard.jwl.util.ext.getAsText

@Component
internal class RepositoryServiceImpl(
	private val cacheFacade: CacheFacade,
	private val i18n: I18n,
	private val environment: BaseEnvironment,
	private val buildInfoSupplier: BuildInfoSupplier,
	private val githubApiService: GithubApiService,
) : RepositoryService {
	private val organizationName = environment
		.getProperty<String>(AppBaseProperty.VCS_ORGANIZATION_NAME)

	override fun getAllRepositories(language: String?): List<RepositoryResDto> {
		val projectsBuildInfo = buildInfoSupplier.fetchProjectsBuildInfo()
			.associateBy { it.name }

		val cachedRepositories = cacheFacade.getCachedList(
			cacheKey = CacheEntity.REPOSITORIES,
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
		val declaredRepos = VcsProperty.entries.map { environment.getProperty<String>(it) }

		val repositories = githubApiService.performGithubGetRequest("/orgs/$organizationName/repos")
		val colors = githubApiService.getGithubProgrammingLanguageColors()

		if (repositories == null || colors == null) {
			return emptyList()
		}
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
			val programmingLanguage = repo.getAsText("language")
			val repositoryUrl = repo.getAsText("html_url")
			val repositoryRes = RepositoryResDto(
				name = i18n.tRaw(I18nAppFragmentSource.PROJECT_NAME, arrayOf(name), language),
				slug = name,
				description = i18n.tRaw(I18nAppFragmentSource.PROJECT_DESCRIPTION, arrayOf(name), language),
				link = repositoryUrl,
				primaryLanguage = PrimaryLanguageDto(
					name = programmingLanguage,
					color = if (colors.has(programmingLanguage)) {
						colors.get(programmingLanguage).getAsText("color")
					} else {
						null
					}
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
			createShortSha(buildInfo.latestVersionLong)
		} else {
			null
		}
		shortSha == it.lastUpdate.buildSha
	}

	private fun createShortSha(versionLong: String) = versionLong.substring(0, 7)
}
