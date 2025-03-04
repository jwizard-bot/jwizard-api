package pl.jwizard.jwa.http.rest.route.status.dto

data class AudioNodeStatus(
	val up: Boolean,
	val id: Int,
	val name: String,
	val pool: String,
	val region: String,
	val version: String? = "?",
	val lavaplayerVersion: String? = "?",
	val players: Int? = 0,
	val playingPlayers: Int? = 0,
	val uptime: String? = "?",
	val responseTime: Long? = 0
)
