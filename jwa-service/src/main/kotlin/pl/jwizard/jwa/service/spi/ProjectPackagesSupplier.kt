package pl.jwizard.jwa.service.spi

interface ProjectPackagesSupplier {
	fun getProjectPackagesCount(): Int

	fun getAllProjectPackages(): Map<String, String>
}
