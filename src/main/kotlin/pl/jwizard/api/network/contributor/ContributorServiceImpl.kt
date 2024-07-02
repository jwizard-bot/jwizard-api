/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.contributor

import org.apache.commons.collections4.CollectionUtils
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pl.jwizard.api.cache.CacheEntity
import pl.jwizard.api.cache.CacheService
import pl.jwizard.api.github.GithubApiProperties
import pl.jwizard.api.network.contributor.dto.ContributorData
import pl.jwizard.api.network.contributor.dto.ContributorDataResDto
import pl.jwizard.api.network.contributor.dto.ContributorsDataResDto
import pl.jwizard.api.scaffold.AbstractLoggingBean

@Service
class ContributorServiceImpl(
	private val cacheService: CacheService,
	private val restTemplate: RestTemplate,
	private val githubApiProperties: GithubApiProperties,
) : ContributorService, AbstractLoggingBean(ContributorServiceImpl::class) {

	override fun getAllContributorsWithVariants(): ContributorsDataResDto {
		val invokeOnAbsent: () -> List<ContributorDataResDto> = {
			// get all contributors with all variants
			val allContributors = ContributeVariant.getAssignableVariants()
				.map { getProjectContributors(it) }
				.flatten()

			// get all variants from selected contributor
			val selectedContributorVariants: (nickname: String) -> List<ContributeVariant> = { nickname ->
				allContributors.filter { it.nickname == nickname }.map { it.variant }
			}
			// filter variants from contributors and add "all" for those this who contribute all sub-projects
			allContributors
				.map { contributor ->
					val contributorVariants = selectedContributorVariants(contributor.nickname)
					val contributorHasAllVariants = CollectionUtils.isEqualCollection(
						contributorVariants,
						ContributeVariant.getAssignableVariants(),
					)
					val variants = contributorVariants.toMutableList()
					if (contributorHasAllVariants) {
						variants.add(ContributeVariant.ALL)
					}
					ContributorDataResDto(contributor, variants)
				}
				.distinctBy { it.nickname }
		}
		val contributors = cacheService.getSafelyList(
			entity = CacheEntity.CONTRIBUTORS,
			key = 0,
			clazz = ContributorDataResDto::class,
			invokeOnAbsent,
		)
		return ContributorsDataResDto(
			contributors = contributors,
			allVariants = ContributeVariant.getNamesFromVariants(),
			initVariant = ContributeVariant.ALL.variant
		)
	}

	private fun getProjectContributors(variant: ContributeVariant): List<ContributorData> {
		val url = variant.getUrl(githubApiProperties)
		val responseType = object : ParameterizedTypeReference<List<Map<String, Any?>>>() {}
		val responseBody = restTemplate.exchange(url, HttpMethod.GET, null, responseType).body
			?: return emptyList() // not found any contributors, skipping
		return responseBody
			.filter { it["type"].toString().lowercase() == "user" }
			.map {
				ContributorData(
					nickname = it["login"] as String,
					profileLink = it["html_url"] as String,
					profileImageUrl = it["avatar_url"] as String,
					variant
				)
			}
	}
}
