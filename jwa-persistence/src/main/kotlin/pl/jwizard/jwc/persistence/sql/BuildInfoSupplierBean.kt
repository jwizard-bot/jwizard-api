package pl.jwizard.jwc.persistence.sql

import pl.jwizard.jwa.service.spi.BuildInfoSupplier
import pl.jwizard.jwa.service.spi.dto.ProjectVersionRow
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.persistence.sql.JdbiQueryBean

@SingletonComponent
class BuildInfoSupplierBean(private val jdbiQuery: JdbiQueryBean) : BuildInfoSupplier {
	override fun fetchProjectsBuildInfo(): List<ProjectVersionRow> {
		val sql = "SELECT name, latest_version_long, last_updated_utc FROM projects"
		return jdbiQuery.queryForList(sql, ProjectVersionRow::class)
	}
}
