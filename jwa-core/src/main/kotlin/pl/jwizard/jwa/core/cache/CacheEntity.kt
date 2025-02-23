package pl.jwizard.jwa.core.cache

import pl.jwizard.jwl.property.Property

enum class CacheEntity(override val key: String) : Property {
	CONTRIBUTORS("Contributors.Cache"),
	PROJECT_PACKAGES("ProjectPackages.Cache"),
	REPOSITORIES("Repositories.Cache"),
	OLDEST_REPOSITORY_DATE("OldestRepositoryDate.Cache"),
	;
}
