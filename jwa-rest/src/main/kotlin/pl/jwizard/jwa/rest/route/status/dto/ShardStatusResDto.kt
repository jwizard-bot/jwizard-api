package pl.jwizard.jwa.rest.route.status.dto

data class ShardStatusResDto(
	val up: Boolean,
	val globalShardId: Int,
	val processGroupId: Int,
	val gatewayPing: Long? = 0,
	val servers: Int? = 0,
	val users: Int? = 0,
	val activeAudioPlayers: Int? = 0,
	val audioListeners: Int? = 0,
)
