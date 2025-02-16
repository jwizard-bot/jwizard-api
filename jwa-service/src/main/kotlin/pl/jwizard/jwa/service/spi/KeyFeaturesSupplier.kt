package pl.jwizard.jwa.service.spi

interface KeyFeaturesSupplier {
	fun getKeyFeatures(): Map<String, Boolean>
}
