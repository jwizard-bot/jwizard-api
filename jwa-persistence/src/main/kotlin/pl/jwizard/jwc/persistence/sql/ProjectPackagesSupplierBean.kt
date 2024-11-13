/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.persistence.sql

import pl.jwizard.jwa.service.spi.ProjectPackagesSupplier
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.persistence.sql.JdbcKtTemplateBean

/**
 * Implementation of the [ProjectPackagesSupplier] interface that retrieves the count of project packages
 * from a database.
 *
 * @property jdbcKtTemplateBean A template bean used for executing SQL queries.
 * @author Miłosz Gilga
 */
@SingletonComponent
class ProjectPackagesSupplierBean(private val jdbcKtTemplateBean: JdbcKtTemplateBean) : ProjectPackagesSupplier {

	/**
	 * Retrieves the count of project packages from the database.
	 *
	 * This method executes a SQL query to count the number of entries in the `project_packages` table and returns that
	 * count as an integer.
	 *
	 * @return The total number of project packages in the database.
	 */
	override fun getProjectPackagesCount(): Int {
		val sql = "SELECT COUNT(*) FROM project_packages"
		return jdbcKtTemplateBean.queryForObject(sql, Int::class)
	}
}
