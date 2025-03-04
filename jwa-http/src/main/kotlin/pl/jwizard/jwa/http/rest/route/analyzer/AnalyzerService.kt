package pl.jwizard.jwa.http.rest.route.analyzer

import pl.jwizard.jwa.http.rest.route.analyzer.dto.CombinedAnalyzerStatisticsResDto

interface AnalyzerService {
	fun getCombinedAnalyzerStatistics(): CombinedAnalyzerStatisticsResDto
}
