/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import pl.jwizard.api.config.EnvironmentContextLoader

@EnableScheduling
@EnableCaching
@EnableJdbcRepositories
@SpringBootApplication
class JWizardApiEntrypoint

fun main(args: Array<String>) {
	EnvironmentContextLoader.loadContext()
	runApplication<JWizardApiEntrypoint>(*args)
}
