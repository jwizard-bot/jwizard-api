/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core

import pl.jwizard.jwl.AppRunner
import pl.jwizard.jwl.SpringKtContextFactory
import pl.jwizard.jwl.util.logger

/**
 * Singleton responsible for running the server application. It extends the base [AppRunner] class to initialized
 * server with pre-defined [SpringKtContextFactory] object.
 *
 * @author Miłosz Gilga
 */
object ServerAppRunner : AppRunner() {
	private val log = logger<ServerAppRunner>()

	/**
	 * Executes the application logic with the provided Spring context. This method is responsible for initializing the
	 * server and setting up the Spring context required for the application to run.
	 *
	 * @param context The pre-configured [SpringKtContextFactory] class representing Spring Context.
	 */
	override fun runWithContext(context: SpringKtContextFactory) {
		log.info("Initialize server with Spring context")
	}
}
