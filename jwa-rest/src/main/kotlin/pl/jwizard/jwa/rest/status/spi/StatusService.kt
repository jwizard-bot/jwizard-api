/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.status.spi

import pl.jwizard.jwa.rest.status.dto.GlobalStatusResDto

/**
 * Interface providing the contract for services responsible for fetching the global status. Implementing classes
 * should define the logic to retrieve the global application status.
 *
 * @author Miłosz Gilga
 */
interface StatusService {

	/**
	 * Retrieves the global status of the application based on the requested language.
	 *
	 * This method should return status information, possibly including service health, shard status, and other critical
	 * application information.
	 *
	 * @param language The language code used to retrieve localized status information.
	 * @return A [GlobalStatusResDto] object containing the global status data in the specified language.
	 */
	fun getGlobalStatus(language: String?): GlobalStatusResDto
}
