package pl.jwizard.jwa.app

import pl.jwizard.jwa.core.ServerAppRunner
import pl.jwizard.jwl.AppContextInitiator

@AppContextInitiator
class JWizardApiEntrypoint

fun main() {
	ServerAppRunner.run(JWizardApiEntrypoint::class)
}
