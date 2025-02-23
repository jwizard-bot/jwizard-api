package pl.jwizard.jwa.rest.route.analyzer

import pl.jwizard.jwa.rest.route.analyzer.dto.CombinedAnalyzerStatisticsResDto

interface AnalyzerService {
	fun getCombinedAnalyzerStatistics(): CombinedAnalyzerStatisticsResDto
}
