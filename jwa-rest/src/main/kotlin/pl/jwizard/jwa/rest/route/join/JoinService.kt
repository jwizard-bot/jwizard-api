package pl.jwizard.jwa.rest.route.join

import pl.jwizard.jwa.rest.route.join.dto.JoinInstanceResDto

interface JoinService {
	fun fetchJoinInstances(): List<JoinInstanceResDto>

	fun fetchRequiredPermissions(): List<String>
}
