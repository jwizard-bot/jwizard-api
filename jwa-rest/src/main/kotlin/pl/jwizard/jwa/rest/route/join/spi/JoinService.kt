package pl.jwizard.jwa.rest.route.join.spi

import pl.jwizard.jwa.rest.route.join.dto.JoinInstanceResDto

interface JoinService {
	fun fetchJoinInstances(): List<JoinInstanceResDto>

	fun fetchRequiredPermissions(): List<String>
}
