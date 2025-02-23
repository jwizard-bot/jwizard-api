package pl.jwizard.jwa.service

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacade
import pl.jwizard.jwa.core.i18n.I18nAppFragmentSource
import pl.jwizard.jwa.rest.route.contributor.ContributorService
import pl.jwizard.jwa.rest.route.contributor.dto.ContributorsResDto
import pl.jwizard.jwa.rest.route.contributor.dto.ProjectContributor
import pl.jwizard.jwa.rest.route.contributor.dto.ProjectVariant
import pl.jwizard.jwl.i18n.I18n
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.property.VcsProperty
import pl.jwizard.jwl.util.ext.getAsText

@Component
internal class ContributorServiceImpl(
	private val environment: BaseEnvironment,
	private val cacheFacade: CacheFacade,
	private val i18n: I18n,
	private val githubApiService: GithubApiService,
) : ContributorService {
	private val defaultVariant = environment.getProperty<String>(VcsProperty.VCS_ALL)
	private val organizationName = environment
		.getProperty<String>(AppBaseProperty.VCS_ORGANIZATION_NAME)

	private val allRepositoryNames = VcsProperty.entries
		.filter { it != VcsProperty.VCS_ALL }
		.map { environment.getProperty<String>(it) }

	override fun getProjectContributors(language: String?): ContributorsResDto {
		val repositoryVariants = VcsProperty.entries
			.associate {
				val repoName = environment.getProperty<String>(it)
				repoName to ProjectVariant(
					name = i18n.tRaw(I18nAppFragmentSource.PROJECT_NAME, arrayOf(repoName), language),
					position = it.ordinal,
				)
			}
			.toMutableMap()
		val contributors = cacheFacade.getCachedList(CacheEntity.CONTRIBUTORS, ::computeOnAbsent)
		return ContributorsResDto(
			contributors = contributors,
			variants = repositoryVariants.toMap(),
			initVariant = defaultVariant,
		)
	}

	private fun computeOnAbsent(): List<ProjectContributor> {
		val contributors = mutableListOf<Pair<String, JsonNode>>()
		for (repositoryName in allRepositoryNames) {
			for (contributor in getPerProjectContributors(repositoryName)) {
				contributors += Pair(repositoryName, contributor)
			}
		}
		val contributorsWithVariants = contributors.map { (_, contributor) ->
			val contributorVariants = contributors
				.filter { (_, user) -> user.getAsText("login") == contributor.getAsText("login") }
				.map { (repository, _) -> repository }
				.toMutableList()

			val memberContributeAllProjects = contributorVariants.containsAll(allRepositoryNames)
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

	private fun getPerProjectContributors(repositoryName: String): MutableList<JsonNode> {
		val jsonNode = githubApiService
			.performGithubGetRequest("/repos/$organizationName/$repositoryName/contributors")
			?: mutableListOf()
		return jsonNode.filter { it.getAsText("type") == "User" }.toMutableList()
	}
}
