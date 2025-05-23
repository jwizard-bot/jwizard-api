package pl.jwizard.jwa.gateway.http.rest.route.join

import pl.jwizard.jwa.gateway.http.rest.route.join.dto.JoinInstanceResDto

interface JoinService {
	fun fetchJoinInstances(avatarSize: Int?): List<JoinInstanceResDto>

	fun fetchRequiredPermissions(): List<String>
}
