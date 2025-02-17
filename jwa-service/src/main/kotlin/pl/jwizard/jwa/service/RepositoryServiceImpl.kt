package pl.jwizard.jwa.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacade
import pl.jwizard.jwa.core.i18n.I18nAppFragmentSource
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.rest.route.repository.RepositoryService
import pl.jwizard.jwa.rest.route.repository.dto.LastUpdateDto
import pl.jwizard.jwa.rest.route.repository.dto.PrimaryLanguageDto
import pl.jwizard.jwa.rest.route.repository.dto.RepositoryResDto
import pl.jwizard.jwa.service.spi.BuildInfoSupplier
import pl.jwizard.jwa.service.spi.dto.ProjectVersionRow
import pl.jwizard.jwl.i18n.I18n
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.property.VcsProperty
import pl.jwizard.jwl.util.ext.getAsText
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
internal class RepositoryServiceImpl(
	private val cacheFacade: CacheFacade,
	private val i18n: I18n,
	private val httpClient: HttpClient,
	private val objectMapper: ObjectMapper,
	private val environment: BaseEnvironment,
	private val buildInfoSupplier: BuildInfoSupplier,
) : RepositoryService {
	private val githubApiUrl = environment.getProperty<String>(ServerProperty.GITHUB_API_URL)
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
		val declaredRepos = VcsProperty.entries.map { environment.getProperty<String>(it) }

		val reposHttpRequest = HttpRequest.newBuilder()
			.uri(URI.create("$githubApiUrl/orgs/$organizationName/repos"))
			.build()

		val colorsHttpRequest = HttpRequest.newBuilder()
			.uri(URI.create(githubLanguageColorApiUrl))
			.build()

		val reposResponse = httpClient.send(reposHttpRequest, HttpResponse.BodyHandlers.ofString())
		val colorsResponse = httpClient.send(colorsHttpRequest, HttpResponse.BodyHandlers.ofString())

		if (reposResponse.statusCode() != 200 || colorsResponse.statusCode() != 200) {
			return emptyList()
		}
		val repositories = objectMapper.readTree(reposResponse.body())
		val colors = objectMapper.readTree(colorsResponse.body())

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
			val color = if (colors.has(programmingLanguage)) {
				colors.get(programmingLanguage).getAsText("color")
			} else {
				null
			}
			val repositoryUrl = repo.getAsText("html_url")
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
			createShortSha(buildInfo.latestVersionLong)
		} else {
			null
		}
		shortSha == it.lastUpdate.buildSha
	}

	private fun createShortSha(versionLong: String) = versionLong.substring(0, 7)
}
