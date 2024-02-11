/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.jwizard.api.scaffold.AbstractLoggingBean

@Service
class StatelessUserDetailsService(
	private val securityProperties: SecurityProperties,
	private val passwordEncoder: PasswordEncoder,
) : AbstractLoggingBean(StatelessUserDetailsService::class), UserDetailsService {

	override fun loadUserByUsername(username: String): UserDetails {
		if (username.startsWith("APP_ID=")) {
			val standaloneClient = securityProperties.standaloneClients
				.find { it.appId == username.substringAfter("=") }
				?: throw UsernameNotFoundException("User with username $username not found.")
			return User.builder()
				.username(standaloneClient.appId)
				.password(passwordEncoder.encode(standaloneClient.appSecret))
				.roles(SpringSecurityConfigurer.USER, SpringSecurityConfigurer.STANDALONE_CLIENT)
				.build()
		}
		return User.builder()
			.username("dawd")
			.password(passwordEncoder.encode("dawdad"))
			.build()
	}
}
