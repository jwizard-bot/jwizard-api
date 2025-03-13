package pl.jwizard.jwa.app

import pl.jwizard.jwl.AppRunner
import pl.jwizard.jwl.ioc.AppContextInitiator

@AppContextInitiator
class JWizardApiEntrypoint

fun main() {
	AppRunner.run(JWizardApiEntrypoint::class)
}
