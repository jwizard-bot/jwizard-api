/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.persistence.sql

import pl.jwizard.jwa.service.spi.BuildInfoSupplier
import pl.jwizard.jwa.service.spi.dto.ProjectVersionRow
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.persistence.sql.JdbiQueryBean

/**
 * Implementation of [BuildInfoSupplier] that fetches build information from a SQL database.
 *
 * This class provides the actual implementation for retrieving project build information from a database. It queries
 * the database to obtain information such as the project name, latest version, and the last update timestamp.
 *
 * @property jdbiQuery The [JdbiQueryBean] used to execute SQL queries and retrieve results.
 * @author Miłosz Gilga
 */
@SingletonComponent
class BuildInfoSupplierBean(private val jdbiQuery: JdbiQueryBean) : BuildInfoSupplier {

	/**
	 * Fetches the build information for multiple projects from the database.
	 *
	 * This method retrieves a list of [ProjectVersionRow] objects, each containing the name, latest version, and the
	 * last updated timestamp for each project stored in the database.
	 *
	 * @return A list of [ProjectVersionRow] containing the build information for each project.
	 */
	override fun fetchProjectsBuildInfo(): List<ProjectVersionRow> {
		val sql = "SELECT name, latest_version_long, last_updated_utc FROM projects"
		return jdbiQuery.queryForList(sql, ProjectVersionRow::class)
	}
}
