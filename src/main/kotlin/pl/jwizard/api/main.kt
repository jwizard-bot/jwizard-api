/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableMongoRepositories
@SpringBootApplication
class JWizardApiEntrypoint

fun main(args: Array<String>) {
	runApplication<JWizardApiEntrypoint>(*args)
}
