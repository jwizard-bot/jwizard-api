package pl.jwizard.jwc.persistence.sql

import pl.jwizard.jwa.service.spi.KeyFeaturesSupplier
import pl.jwizard.jwl.ioc.stereotype.SingletonComponent
import pl.jwizard.jwl.persistence.sql.ColumnDef
import pl.jwizard.jwl.persistence.sql.JdbiQueryBean

@SingletonComponent
class KeyFeaturesSupplierBean(private val jdbiQuery: JdbiQueryBean) : KeyFeaturesSupplier {
	override fun getKeyFeatures() = jdbiQuery.queryForListMap(
		sql = "SELECT name, is_active FROM key_features",
		key = ColumnDef("name", String::class),
		value = ColumnDef("is_active", Boolean::class)
	)
}
