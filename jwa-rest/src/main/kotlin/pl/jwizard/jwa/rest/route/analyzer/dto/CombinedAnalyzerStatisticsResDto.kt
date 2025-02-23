package pl.jwizard.jwa.rest.route.analyzer.dto

data class CombinedAnalyzerStatisticsResDto(
	val totalLines: Long,
	val totalFiles: Long,
	val countOfProjects: Int,
	val monthsInDevelopment: Int,
)
