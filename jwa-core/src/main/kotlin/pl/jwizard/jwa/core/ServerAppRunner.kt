/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core

import org.springframework.context.annotation.ComponentScan
import pl.jwizard.jwa.core.printer.AbstractPrinter
import pl.jwizard.jwa.core.printer.ConsolePrinter
import pl.jwizard.jwa.core.printer.FancyFramePrinter
import pl.jwizard.jwa.core.printer.FancyTitlePrinter
import pl.jwizard.jwa.core.util.logger
import kotlin.reflect.KClass

/**
 * The main class that loads resources, configuration and runs the server.
 * Use this class with [ServerApp] annotation. Singleton instance.
 *
 * @author Miłosz Gilga
 */
object ServerAppRunner {
	private val log = logger<ServerAppRunner>()

	/**
	 * Base application package. Used for Spring Context [ComponentScan] annotation. All classes related with
	 * Spring IoC containers will be loaded into Spring Context.
	 */
	const val BASE_PACKAGE = "pl.jwizard"

	/**
	 * Fancy title banner classpath location in `resources` directory.
	 */
	private const val BANNER_CLASSPATH_LOCATION = "util/banner.txt"

	/**
	 * Fancy frame classpath location in `resources` directory.
	 */
	private const val FRAME_CLASSPATH_LOCATION = "util/frame.txt"

	/**
	 * Static method which starts loading configuration, resources and a new Server instance.
	 *
	 * @param clazz Main type of class that runs the server.
	 */
	fun run(clazz: KClass<*>) {
		val startTimestamp = System.currentTimeMillis()
		val printer = ConsolePrinter()
		val printers = arrayOf(
			FancyTitlePrinter(BANNER_CLASSPATH_LOCATION, printer),
			FancyFramePrinter(FRAME_CLASSPATH_LOCATION, printer),
		)
		AbstractPrinter.printContent(printers)
		try {
			// create context, load properties and create server

			val endTimestamp = System.currentTimeMillis()
			val elapsedTime = endTimestamp - startTimestamp

			log.info("Load in: {}s. Start listening incoming requests...", elapsedTime / 1000.0)
		} catch (ex: IrreparableException) {
			ex.printLogStatement()
			ex.killProcess()
		}
	}
}
