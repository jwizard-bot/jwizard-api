package pl.jwizard.jwa.service.spi

import pl.jwizard.jwa.service.spi.dto.ProjectVersionRow

interface BuildInfoSupplier {
	fun fetchProjectsBuildInfo(): List<ProjectVersionRow>
}
