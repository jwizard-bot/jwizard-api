package pl.jwizard.jwa.service

import com.fasterxml.jackson.databind.JsonNode
import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacadeBean
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwa.rest.route.contributor.dto.ContributorsResDto
import pl.jwizard.jwa.rest.route.contributor.dto.ProjectContributor
import pl.jwizard.jwa.rest.route.contributor.dto.ProjectVariant
import pl.jwizard.jwa.rest.route.contributor.spi.ContributorService
import pl.jwizard.jwl.http.HttpClientFacadeBean
import pl.jwizard.jwl.i18n.I18nBean
import pl.jwizard.jwl.i18n.source.I18nLibDynamicSource
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.util.ext.getAsText
import pl.jwizard.jwl.vcs.VcsConfigBean
import pl.jwizard.jwl.vcs.VcsRepository

@SingletonService
class ContributorServiceBean(
	private val environment: EnvironmentBean,
	private val cacheFacade: CacheFacadeBean,
	private val httpClientFacade: HttpClientFacadeBean,
	private val i18n: I18nBean,
	private val vcsConfig: VcsConfigBean,
) : ContributorService {
	private val githubApiUrl = environment
		.getProperty<String>(ServerProperty.GITHUB_API_URL)

	private val organizationName = environment
		.getProperty<String>(AppBaseProperty.VCS_ORGANIZATION_NAME)

	private val variants = VcsRepository.entries
		.filter { it != VcsRepository.ALL }
		.map { environment.getProperty<String>(it.property) }

	private val defaultVariant = vcsConfig.getRepositoryName(VcsRepository.ALL)

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

	private fun getPerProjectContributors(repository: VcsRepository): MutableList<JsonNode> {
		val repositoryName = environment.getProperty<String>(repository.property)
		val response = httpClientFacade.getJsonListCall(
			url = "$githubApiUrl/repos/$organizationName/$repositoryName/contributors"
		)
		return response.filter { it.getAsText("type") == "User" }.toMutableList()
	}
}
