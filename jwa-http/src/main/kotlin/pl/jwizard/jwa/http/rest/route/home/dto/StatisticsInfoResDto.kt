package pl.jwizard.jwa.http.rest.route.home.dto

data class StatisticsInfoResDto(
	val modules: Int,
	val commands: Int,
	val radioStations: Int,
	val openSourceLibraries: Int,
)
