package pl.jwizard.jwc.persistence.sql

import pl.jwizard.jwa.service.spi.ProjectPackagesSupplier
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.persistence.sql.ColumnDef
import pl.jwizard.jwl.persistence.sql.JdbiQueryBean

@SingletonComponent
class ProjectPackagesSupplierBean(private val jdbiQuery: JdbiQueryBean) : ProjectPackagesSupplier {
	override fun getProjectPackagesCount(): Int {
		val sql = "SELECT COUNT(DISTINCT name) FROM project_packages"
		return jdbiQuery.queryForObject(sql, Int::class)
	}

	override fun getAllProjectPackages() = jdbiQuery.queryForListMap(
		sql = "SELECT DISTINCT name, link FROM project_packages ORDER BY name",
		key = ColumnDef("name", String::class),
		value = ColumnDef("link", String::class)
	)
}
