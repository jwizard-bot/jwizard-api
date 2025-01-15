/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.rest.route.join.spi

import pl.jwizard.jwa.rest.route.join.dto.JoinInstanceResDto

/**
 * Interface defining the contract for the join service.
 *
 * The service is responsible for fetching join instances and the required permissions for a specific join operation.
 * Implementations of this interface should provide the logic for retrieving these details.
 *
 * @author Miłosz Gilga
 */
interface JoinService {

	/**
	 * Fetches a list of join instances.
	 *
	 * This method retrieves the available joinable instances, returning a list of [JoinInstanceResDto] objects which
	 * include details like the name, color, and link.
	 *
	 * @return A list of join instances with their details.
	 */

	fun fetchJoinInstances(): List<JoinInstanceResDto>

	/**
	 * Fetches a list of required permissions for a join operation.
	 *
	 * This method retrieves the permissions that are needed to perform a join operation.
	 *
	 * @return A list of permissions as strings that are required to join.
	 */
	fun fetchRequiredPermissions(): List<String>
}
