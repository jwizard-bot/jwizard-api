/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core

import pl.jwizard.jwl.AppRunner
import pl.jwizard.jwl.ioc.IoCKtContextFactory
import pl.jwizard.jwl.server.HttpServer

/**
 * Singleton responsible for running the server application. It extends the base [AppRunner] class to initialized
 * server with pre-defined [IoCKtContextFactory] object.
 *
 * @author Miłosz Gilga
 */
object ServerAppRunner : AppRunner() {

	/**
	 * Executes the application logic with the provided Spring context. This method is responsible for initializing the
	 * server and setting up the Spring context required for the application to run.
	 *
	 * @param context The pre-configured [IoCKtContextFactory] class representing IoC context.
	 */
	override fun runWithContext(context: IoCKtContextFactory) {
		val httpServer = context.getBean(HttpServer::class)
		httpServer.init()
	}
}
