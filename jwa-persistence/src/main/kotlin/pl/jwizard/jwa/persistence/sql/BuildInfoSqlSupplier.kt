package pl.jwizard.jwa.persistence.sql

import org.springframework.stereotype.Component
import pl.jwizard.jwa.service.spi.BuildInfoSupplier
import pl.jwizard.jwa.service.spi.dto.ProjectVersionRow
import pl.jwizard.jwl.persistence.sql.JdbiQuery

@Component
class BuildInfoSqlSupplier(private val jdbiQuery: JdbiQuery) : BuildInfoSupplier {
	override fun fetchProjectsBuildInfo(): List<ProjectVersionRow> {
		val sql = "SELECT name, latest_version_long, last_updated_utc FROM projects"
		return jdbiQuery.queryForList(sql, ProjectVersionRow::class)
	}
}
