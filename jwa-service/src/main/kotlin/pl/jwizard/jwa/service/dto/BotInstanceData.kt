package pl.jwizard.jwa.service.dto

data class BotInstanceData(
	val appId: String,
	val color: String,
	val instancePrefix: String,
	val shardsPerCluster: Int,
	val shardOverallMax: Int,
)
