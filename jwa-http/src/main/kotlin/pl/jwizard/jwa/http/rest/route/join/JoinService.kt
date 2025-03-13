package pl.jwizard.jwa.http.rest.route.join

import pl.jwizard.jwa.http.rest.route.join.dto.JoinInstanceResDto

interface JoinService {
	fun fetchJoinInstances(avatarSize: Int?): List<JoinInstanceResDto>

	fun fetchRequiredPermissions(): List<String>
}
