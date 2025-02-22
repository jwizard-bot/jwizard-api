package pl.jwizard.jwa.rest.route.status.dto

data class AudioNodesStatusResDto(
	val externalServicesUrl: String,
	val nodes: List<AudioNodeStatus>,
)
