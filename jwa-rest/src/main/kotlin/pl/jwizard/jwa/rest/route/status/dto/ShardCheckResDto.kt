package pl.jwizard.jwa.rest.route.status.dto

data class ShardCheckResDto(
	// true - shard with selected guild is active, false - shard is down or not assigned to
	// searched guild
	val status: Boolean,
)
