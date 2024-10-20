/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.app

import pl.jwizard.jwa.core.ServerApp
import pl.jwizard.jwa.core.ServerAppRunner

/**
 * Main application class.
 *
 * @author Miłosz Gilga
 */
@ServerApp
class JWizardApiEntrypoint

fun main() {
	ServerAppRunner.run(JWizardApiEntrypoint::class)
}
