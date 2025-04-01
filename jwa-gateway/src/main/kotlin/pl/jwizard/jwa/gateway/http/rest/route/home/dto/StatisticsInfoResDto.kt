package pl.jwizard.jwa.gateway.http.rest.route.home.dto

data class StatisticsInfoResDto(
	val modules: Int,
	val commands: Int,
	val radioStations: Int,
	val openSourceLibraries: Int,
)
