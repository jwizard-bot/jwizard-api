package pl.jwizard.jwa.service

import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.cache.CacheEntity
import pl.jwizard.jwa.core.cache.CacheFacade
import pl.jwizard.jwa.rest.route.analyzer.AnalyzerService
import pl.jwizard.jwa.rest.route.analyzer.dto.CombinedAnalyzerStatisticsResDto
import pl.jwizard.jwa.service.spi.ProjectsAnalyzerSupplier
import pl.jwizard.jwl.property.AppBaseProperty
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.property.VcsProperty
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset

@Component
internal class AnalyzerServiceImpl(
	private val githubApiService: GithubApiService,
	private val projectsAnalyzerSupplier: ProjectsAnalyzerSupplier,
	private val cacheFacade: CacheFacade,
	private val environment: BaseEnvironment,
) : AnalyzerService {
	private val organizationName = environment
		.getProperty<String>(AppBaseProperty.VCS_ORGANIZATION_NAME)

	private val allRepositoryNames = VcsProperty.entries
		.filter { it != VcsProperty.VCS_ALL }
		.map { environment.getProperty<String>(it) }

	override fun getCombinedAnalyzerStatistics(): CombinedAnalyzerStatisticsResDto {
		val (totalLines, totalFiles) = projectsAnalyzerSupplier.getProjectFilesAnalysis()
		val allRepositories = projectsAnalyzerSupplier.getProjectsCount()
		val totalMonths = cacheFacade.getCached(
			cacheKey = CacheEntity.OLDEST_REPOSITORY_DATE,
			computeOnAbsent = ::determinateTotalMonths,
		)
		return CombinedAnalyzerStatisticsResDto(totalLines, totalFiles, allRepositories, totalMonths)
	}

	private fun determinateTotalMonths(): Int {
		val repositoriesCreatedDate = mutableListOf<LocalDate>()
		for (repositoryName in allRepositoryNames) {
			val repoNode = githubApiService
				.performGithubGetRequest("/repos/$organizationName/$repositoryName")
			val createdAt = repoNode?.get("created_at")?.asText()
			if (createdAt != null) {
				repositoriesCreatedDate += Instant.parse(createdAt)
					.atZone(ZoneOffset.UTC)
					.toLocalDate()
			}
		}
		val oldestDate = repositoriesCreatedDate.minOrNull() ?: LocalDate.now()
		val period = Period.between(oldestDate, LocalDate.now())
		return period.years * 12 + period.months
	}
}
