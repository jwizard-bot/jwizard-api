/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.persistence.sql

import pl.jwizard.jwa.service.spi.ProjectPackagesSupplier
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.persistence.sql.ColumnDef
import pl.jwizard.jwl.persistence.sql.JdbiQueryBean

/**
 * Implementation of the [ProjectPackagesSupplier] interface that retrieves the count of project packages
 * from a database.
 *
 * @property jdbiQuery Bean for executing SQL queries.
 * @author Miłosz Gilga
 */
@SingletonComponent
class ProjectPackagesSupplierBean(private val jdbiQuery: JdbiQueryBean) : ProjectPackagesSupplier {

	/**
	 * Retrieves the count of project packages from the database.
	 *
	 * This method executes a SQL query to count the number of entries in the `project_packages` table and returns that
	 * count as an integer.
	 *
	 * @return The total number of project packages in the database.
	 */
	override fun getProjectPackagesCount(): Int {
		val sql = "SELECT COUNT(DISTINCT name) FROM project_packages"
		return jdbiQuery.queryForObject(sql, Int::class)
	}

	/**
	 * Retrieves all project packages from the database.
	 *
	 * This method executes a SQL query to retrieve the name and link of each package in the `project_packages` table and
	 * returns them as a map.
	 *
	 * @return A [Map] containing the name and link of each package.
	 */
	override fun getAllProjectPackages() = jdbiQuery.queryForListMap(
		sql = "SELECT DISTINCT name, link FROM project_packages ORDER BY name",
		key = ColumnDef("name", String::class),
		value = ColumnDef("link", String::class)
	)
}
