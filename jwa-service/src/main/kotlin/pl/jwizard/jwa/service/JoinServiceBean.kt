/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import net.dv8tion.jda.api.Permission
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.rest.route.join.dto.JoinInstanceResDto
import pl.jwizard.jwa.rest.route.join.spi.JoinService
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseListProperty
import pl.jwizard.jwl.util.UrlSearchParams

/**
 * Service bean responsible for managing the join-related logic and fetching the required bot instances and permissions
 * for the join functionality.
 *
 * @property environment The environment bean used to access configuration properties.
 * @author Miłosz Gilga
 */
@SingletonService
class JoinServiceBean(
	private val environment: EnvironmentBean,
	private val botInstancesService: BotInstancesServiceBean
) : JoinService {

	/**
	 * List of permissions required for the join operation, fetched from the environment configuration.
	 */
	private val permissions = environment.getListProperty<String>(AppBaseListProperty.JDA_PERMISSIONS)

	/**
	 * Fetches a list of joinable instances by mapping each bot instance to a [JoinInstanceResDto] object. This method
	 * constructs a join URL for each bot instance with the appropriate permissions.
	 *
	 * @return A list of [JoinInstanceResDto] objects representing the joinable bot instances.
	 */
	override fun fetchJoinInstances(): List<JoinInstanceResDto> {
		val mappedPermissions = permissions.map { Permission.valueOf(it) }
		val rawPermissions = Permission.getRaw(mappedPermissions)

		return botInstancesService.botInstances.entries.map { (id, data) ->
			val instanceIndex = if (id > 0) " ($id)" else ""

			val joinLink = UrlSearchParams.Builder()
				.setBaseUrl("https://discord.com/oauth2/authorize")
				.addParam("client_id", data.appId)
				.addParam("scope", "bot")
				.addParam("permissions", rawPermissions)
				.build()

			JoinInstanceResDto(
				name = "JWizard$instanceIndex",
				color = data.color,
				link = joinLink
			)
		}
	}

	/**
	 * Fetches the required permissions for a join operation. The permissions are sorted before returning.
	 *
	 * @return A sorted list of permissions required for the join operation.
	 */
	override fun fetchRequiredPermissions() = permissions.sorted()
}
