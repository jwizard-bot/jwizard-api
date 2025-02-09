/*
 * Copyright (c) 2025 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.service

import pl.jwizard.jwa.core.property.EnvironmentBean
import pl.jwizard.jwa.service.dto.BotInstanceData
import pl.jwizard.jwl.ioc.stereotype.SingletonService
import pl.jwizard.jwl.property.vault.VaultClient
import pl.jwizard.jwl.util.getUserIdFromToken

/**
 * Service responsible for managing bot instances. It interacts with the Vault to retrieve and store bot instance
 * configurations.
 *
 * @property environment The environment configuration bean.
 * @author Miłosz Gilga
 */
@SingletonService
class BotInstancesServiceBean(private val environment: EnvironmentBean) {

	/**
	 * Vault client used to interact with the vault for fetching configuration and bot instance data.
	 */
	private val vaultClient = VaultClient(environment)

	/**
	 * Regular expression pattern used to filter bot instance keys in the Vault storage.
	 */
	private val instancePattern = Regex("^core-instance-\\d+$")

	/**
	 * Map storing bot instances with their corresponding IDs and data.
	 */
	final val botInstances = mutableMapOf<Int, BotInstanceData>()

	init {
		vaultClient.init()
		botInstances += getBotInstancesData()
	}

	/**
	 * Retrieves bot instance data from the Vault storage.
	 *
	 * @return A map of bot instance IDs and their corresponding data.
	 */
	private fun getBotInstancesData(): Map<Int, BotInstanceData> {
		val botInstances = vaultClient.readKvPaths(patternFilter = instancePattern)
		val fetchedInstances = mutableMapOf<Int, BotInstanceData>()
		for (botInstance in botInstances) {
			val instanceId = botInstance.substringAfterLast("-").toInt()
			val instanceProperties = vaultClient.readKvSecrets(botInstance)
			val appId = getUserIdFromToken(instanceProperties.getProperty("V_JDA_SECRET")) ?: continue
			fetchedInstances[instanceId] = BotInstanceData(
				appId,
				color = "#%06X".format(Integer.decode(instanceProperties.getProperty("V_JDA_PRIMARY_COLOR"))),
			)
		}
		return fetchedInstances
	}
}
