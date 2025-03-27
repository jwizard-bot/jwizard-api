package pl.jwizard.jwa.persistence.sql

import org.springframework.stereotype.Component
import pl.jwizard.jwa.service.spi.KeyFeaturesSupplier
import pl.jwizard.jwl.persistence.sql.ColumnDef
import pl.jwizard.jwl.persistence.sql.JdbiQuery

@Component
class KeyFeaturesSqlSupplier(private val jdbiQuery: JdbiQuery) : KeyFeaturesSupplier {
	override fun getKeyFeatures() = jdbiQuery.queryForListMap(
		sql = "SELECT name, is_active FROM key_features",
		key = ColumnDef("name", String::class),
		value = ColumnDef("is_active", Boolean::class)
	)
}
