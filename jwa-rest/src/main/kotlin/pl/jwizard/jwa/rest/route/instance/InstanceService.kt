package pl.jwizard.jwa.rest.route.instance

import pl.jwizard.jwa.rest.dto.OptionsResDto
import pl.jwizard.jwa.rest.route.instance.dto.InstanceDefinitionResDto
import pl.jwizard.jwa.rest.route.instance.dto.InstanceOption

interface InstanceService {
	fun getAllInstanceOptions(language: String?): OptionsResDto<InstanceOption>

	fun getAllInstanceIds(): List<Int>

	fun getAllInstanceDefinitions(avatarSize: Int?): List<InstanceDefinitionResDto>
}
