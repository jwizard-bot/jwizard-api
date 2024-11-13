/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.app

import pl.jwizard.jwa.core.ServerAppRunner
import pl.jwizard.jwl.AppContextInitiator

/**
 * Main application class.
 *
 * @author Miłosz Gilga
 */
@AppContextInitiator
class JWizardApiEntrypoint

fun main() {
	ServerAppRunner.run(JWizardApiEntrypoint::class)
}
