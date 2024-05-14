/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.deployment

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwizard.deployment")
data class DeploymentProperties(
	var buildVersion: String = "UNKNOWN",
	var lastBuildDate: String = "UNKNOWN",
)
