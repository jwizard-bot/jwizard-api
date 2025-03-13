package pl.jwizard.jwa.core

import io.javalin.config.JavalinConfig
import io.javalin.plugin.bundled.CorsPluginConfig
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerListProperty
import pl.jwizard.jwl.AppInitiator
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.server.HttpServer
import pl.jwizard.jwl.util.logger

@Component
internal class MainAppInitiator(
	private val httpServer: HttpServer,
	environment: BaseEnvironment,
) : AppInitiator {
	companion object {
		private val log = logger<MainAppInitiator>()
	}

	private val corsUrls = environment.getListProperty<String>(ServerListProperty.CORS_URLS)

	private fun createServerConfig(config: JavalinConfig) {
		val corsRules: (CorsPluginConfig) -> Unit = { cors ->
			cors.addRule {
				it.allowHost(corsUrls.first(), *(corsUrls.drop(1).toTypedArray()))
				it.allowCredentials = true
			}
		}
		// enable cors only if cors urls is not empty
		if (corsUrls.isNotEmpty()) {
			config.bundledPlugins.enableCors(corsRules)
			log.info("Cors urls: \"{}\".", corsUrls)
		}
	}

	override fun onInit() {
		httpServer.init(extendedConfig = ::createServerConfig)
	}
}
