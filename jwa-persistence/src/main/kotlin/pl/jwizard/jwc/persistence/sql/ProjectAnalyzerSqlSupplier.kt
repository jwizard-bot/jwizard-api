package pl.jwizard.jwc.persistence.sql

import org.springframework.stereotype.Component
import pl.jwizard.jwa.service.spi.ProjectsAnalyzerSupplier
import pl.jwizard.jwa.service.spi.dto.ProjectFilesAnalysis
import pl.jwizard.jwl.persistence.sql.JdbiQuery

@Component
class ProjectAnalyzerSqlSupplier(private val jdbiQuery: JdbiQuery) : ProjectsAnalyzerSupplier {
	override fun getProjectsCount() = jdbiQuery.queryForObject(
		sql = "SELECT COUNT(id) FROM projects",
		type = Int::class,
	)

	override fun getProjectFilesAnalysis() = jdbiQuery.queryForObject(
		sql = """
			SELECT SUM(files_count) totalFiles, SUM(lines_count) totalLines
			FROM project_analyzer_results
		""",
		type = ProjectFilesAnalysis::class,
	)
}
