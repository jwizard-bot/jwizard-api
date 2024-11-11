/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwc.persistence.sql

import pl.jwizard.jwa.service.spi.KeyFeaturesSupplier
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.persistence.sql.ColumnDef
import pl.jwizard.jwl.persistence.sql.JdbcKtTemplateBean

/**
 * Implementation of the [KeyFeaturesSupplier] interface that retrieves key feature data from a database.
 *
 * @property jdbcKtTemplateBean A template bean used for executing SQL queries.
 * @author Miłosz Gilga
 */
@SingletonComponent
class KeyFeaturesSupplierBean(private val jdbcKtTemplateBean: JdbcKtTemplateBean) : KeyFeaturesSupplier {

	/**
	 * Retrieves key features from the database.
	 *
	 * This method executes a SQL query to fetch all features from the `key_features` table. The query results are
	 * returned as a map, where the key is the feature name (String) and the value indicates whether the feature is
	 * active (Boolean).
	 *
	 * @return A map containing feature names as keys and their active status (Boolean) as values.
	 */
	override fun getKeyFeatures() = jdbcKtTemplateBean.queryForListMap(
		sql = "SELECT name, is_active FROM key_features",
		key = ColumnDef("name", String::class),
		value = ColumnDef("is_active", Boolean::class)
	)
}
