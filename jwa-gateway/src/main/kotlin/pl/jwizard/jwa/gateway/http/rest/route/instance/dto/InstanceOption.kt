package pl.jwizard.jwa.gateway.http.rest.route.instance.dto

data class InstanceOption(
	// -1 in id means all
	val id: Int,
	val name: String,
)
