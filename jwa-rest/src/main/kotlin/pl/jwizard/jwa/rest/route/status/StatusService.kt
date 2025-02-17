package pl.jwizard.jwa.rest.route.status

import pl.jwizard.jwa.rest.route.status.dto.GlobalStatusResDto

interface StatusService {
	fun getGlobalStatus(language: String?): GlobalStatusResDto
}
