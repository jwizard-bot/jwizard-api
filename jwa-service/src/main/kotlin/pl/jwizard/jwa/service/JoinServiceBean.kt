/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import net.dv8tion.jda.api.Permission
import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.rest.route.join.dto.JoinInstanceResDto
import pl.jwizard.jwa.rest.route.join.spi.JoinService
import pl.jwizard.jwa.service.dto.BotInstanceData
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.AppBaseListProperty
import pl.jwizard.jwl.property.vault.VaultClient
import pl.jwizard.jwl.util.UrlSearchParams
import pl.jwizard.jwl.util.getUserIdFromToken

/**
 * Service bean responsible for managing the join-related logic and fetching the required bot instances and permissions
 * for the join functionality.
 *
 * @property environment The environment bean used to access configuration properties.
 * @author Miłosz Gilga
 */
@SingletonService
class JoinServiceBean(private val environment: EnvironmentBean) : JoinService {

	/**
	 * Vault client used to interact with the vault for fetching configuration and bot instance data.
	 */
	private val vaultClient = VaultClient(environment)

	/**
	 * List of bot instances that are used for the join functionality.
	 */
	private val botInstances = mutableListOf<BotInstanceData>()

	/**
	 * List of permissions required for the join operation, fetched from the environment configuration.
	 */
	private val permissions = environment.getListProperty<String>(AppBaseListProperty.JDA_PERMISSIONS)

	init {
		vaultClient.init()
		botInstances += getDeclaredBotInstances()
	}

	/**
	 * Fetches a list of joinable instances by mapping each bot instance to a [JoinInstanceResDto] object. This method
	 * constructs a join URL for each bot instance with the appropriate permissions.
	 *
	 * @return A list of [JoinInstanceResDto] objects representing the joinable bot instances.
	 */
	override fun fetchJoinInstances(): List<JoinInstanceResDto> {
		val mappedPermissions = permissions.map { Permission.valueOf(it) }
		val rawPermissions = Permission.getRaw(mappedPermissions)

		return botInstances.mapIndexed { index, instance ->
			val instanceIndex = if (index > 0) " ($index)" else ""

			val joinLink = UrlSearchParams.Builder()
				.setBaseUrl("https://discord.com/oauth2/authorize")
				.addParam("client_id", instance.appId)
				.addParam("scope", "bot")
				.addParam("permissions", rawPermissions)
				.build()

			JoinInstanceResDto(
				name = "JWizard$instanceIndex",
				color = instance.color,
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

	/**
	 * Retrieves the declared bot instances from the vault and converts them into [BotInstanceData] objects. Each bot
	 * instance is fetched using its unique key pattern and its properties are processed.
	 *
	 * @return A list of [BotInstanceData] objects representing the bot instances.
	 */
	private fun getDeclaredBotInstances(): List<BotInstanceData> {
		val botInstances = vaultClient.readKvPaths(patternFilter = Regex("^core-instance-\\d+$"))
		val fetchedInstances = mutableListOf<BotInstanceData>()
		for (botInstance in botInstances) {
			val instanceProperties = vaultClient.readKvSecrets(botInstance)
			val appId = getUserIdFromToken(instanceProperties.getProperty("V_JDA_SECRET")) ?: continue
			val color = Integer.decode(instanceProperties.getProperty("V_JDA_PRIMARY_COLOR"))
			fetchedInstances += BotInstanceData(
				appId,
				color = "#%06X".format(color),
			)
		}
		return fetchedInstances
	}
}
