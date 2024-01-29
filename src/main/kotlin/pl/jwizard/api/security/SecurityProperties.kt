/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwizard.security")
data class SecurityProperties(
	var jwtSecret: String = "",
	var jwtIssuer: String = "",
	var jwtAudience: TokenAudience = TokenAudience("proxy://standalone", "http://localhost:3000"),
	var life: TokenLife = TokenLife(5, 3),
	var standaloneClients: List<StandaloneClient> = listOf()
)

data class StandaloneClient(
	var appId: String,
	var appSecret: String,
)

data class TokenLife(
	var accessMinutes: Int,
	var refreshDays: Int,
)

data class TokenAudience(
	var standaloneClient: String,
	var webClient: String
)
