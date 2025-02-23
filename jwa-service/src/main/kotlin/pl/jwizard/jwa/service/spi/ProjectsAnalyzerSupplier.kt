package pl.jwizard.jwa.service.spi

import pl.jwizard.jwa.service.spi.dto.ProjectFilesAnalysis

interface ProjectsAnalyzerSupplier {
	fun getProjectsCount(): Int

	fun getProjectFilesAnalysis(): ProjectFilesAnalysis
}
