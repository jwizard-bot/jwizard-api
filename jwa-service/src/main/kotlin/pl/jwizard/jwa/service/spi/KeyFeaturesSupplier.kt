/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service.spi

/**
 * Interface for supplying key features of the system.
 *
 * @author Miłosz Gilga
 */
interface KeyFeaturesSupplier {

	/**
	 * Retrieves a map of key features with their active status.
	 *
	 * This method returns a map where the key represents the feature's identifier (ex. a feature name or key), and the
	 * value is a Boolean indicating whether the feature is active (`true`) or inactive (`false`).
	 *
	 * @return A map of feature identifiers as [String] keys, with their corresponding active status as [Boolean] values.
	 */
	fun getKeyFeatures(): Map<String, Boolean>
}
