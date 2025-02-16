package pl.jwizard.jwa.core

import pl.jwizard.jwl.AppRunner
import pl.jwizard.jwl.ioc.IoCKtContextFactory
import pl.jwizard.jwl.server.HttpServer

object ServerAppRunner : AppRunner() {
	override fun runWithContext(context: IoCKtContextFactory) {
		val httpServer = context.getBean(HttpServer::class)
		httpServer.init()
	}
}
