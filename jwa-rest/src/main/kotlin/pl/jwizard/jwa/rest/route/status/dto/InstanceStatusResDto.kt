package pl.jwizard.jwa.rest.route.status.dto

data class InstanceStatusResDto(
	val id: Int,
	val name: String,
	val color: String,
	val avatarUrl: String,
	val shards: RunningStatusCount,
	val processes: RunningStatusCount,
	val avgShardGatewayPing: Long,
	val avgShardsPerProcess: Int,
	val servers: Int,
	val users: Int,
	val activeAudioPlayers: Int,
	val audioListeners: Int,
)
