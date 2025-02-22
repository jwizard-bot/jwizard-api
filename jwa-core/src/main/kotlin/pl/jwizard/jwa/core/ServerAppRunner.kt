package pl.jwizard.jwa.core

import pl.jwizard.jwa.core.property.ServerListProperty
import pl.jwizard.jwl.AppRunner
import pl.jwizard.jwl.ioc.IoCKtContextFactory
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.server.HttpServer
import pl.jwizard.jwl.util.logger

object ServerAppRunner : AppRunner() {
	private val log = logger<ServerAppRunner>()

	override fun runWithContext(context: IoCKtContextFactory) {
		val httpServer = context.getBean(HttpServer::class)
		val environment = context.getBean(BaseEnvironment::class)

		val corsUrls = environment.getListProperty<String>(ServerListProperty.CORS_URLS)
		log.info("Loaded cors urls: \"$corsUrls\".")

		httpServer.init({ config ->
			config.bundledPlugins.enableCors { cors ->
				if (corsUrls.isNotEmpty()) {
					cors.addRule { it.allowHost(corsUrls.first(), *(corsUrls.drop(1).toTypedArray())) }
				}
			}
		})
	}
}
