package pl.jwizard.jwa.gateway.http.rest.route.analyzer

import pl.jwizard.jwa.gateway.http.rest.route.analyzer.dto.CombinedAnalyzerStatisticsResDto

interface AnalyzerService {
	fun getCombinedAnalyzerStatistics(): CombinedAnalyzerStatisticsResDto
}
