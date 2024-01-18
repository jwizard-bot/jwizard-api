/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JWizardApiEntrypoint

fun main(args: Array<String>) {
	runApplication<JWizardApiEntrypoint>(*args)
}
