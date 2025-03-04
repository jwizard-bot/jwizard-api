package pl.jwizard.jwa.http.rest.route.instance

import pl.jwizard.jwa.http.dto.OptionsResDto
import pl.jwizard.jwa.http.rest.route.instance.dto.InstanceDefinitionResDto
import pl.jwizard.jwa.http.rest.route.instance.dto.InstanceOption

interface InstanceService {
	fun getAllInstanceOptions(language: String?): OptionsResDto<InstanceOption>

	fun getAllInstanceIds(): List<Int>

	fun getAllInstanceDefinitions(avatarSize: Int?): List<InstanceDefinitionResDto>
}
