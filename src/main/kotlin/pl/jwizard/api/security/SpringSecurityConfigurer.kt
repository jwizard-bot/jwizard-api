/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutFilter
import pl.jwizard.api.scaffold.AbstractLoggingBean
import pl.jwizard.api.security.filter.JwtAuthenticationFilter
import pl.jwizard.api.security.filter.MiddlewareExceptionFilter
import pl.jwizard.api.security.resolver.AccessDeniedResolver
import pl.jwizard.api.security.resolver.SecurityChainResolver

@Configuration
@EnableWebSecurity
class SpringSecurityConfigurer(
	private val middlewareExceptionFilter: MiddlewareExceptionFilter,
	private val securityChainResolver: SecurityChainResolver,
	private val accessDeniedResolver: AccessDeniedResolver,
	private val jwtAuthenticationFilter: JwtAuthenticationFilter,
	private val passwordEncoder: PasswordEncoder,
	private val messageSource: MessageSource,
) : AbstractLoggingBean(SpringSecurityConfigurer::class) {

	@Bean
	fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
		httpSecurity
			.addFilterBefore(middlewareExceptionFilter, LogoutFilter::class.java)
			.formLogin { it.disable() }
			.httpBasic { it.disable() }
			.csrf { it.disable() }
			.exceptionHandling {
				it
					.authenticationEntryPoint(securityChainResolver)
					.accessDeniedHandler(accessDeniedResolver)
			}
			.securityMatcher("/api/v1/**")
			.authorizeHttpRequests {
				it
					.requestMatchers("/api/v1/deployment").permitAll()
					.requestMatchers("/api/v1/contributor/all").permitAll()
					.requestMatchers("/api/v1/home/stats").permitAll()
					.requestMatchers("/api/v1/home/key-features").permitAll()
					.anyRequest().authenticated()
			}
			.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
		return httpSecurity.build()
	}

	@Bean
	fun authenticationManager(httpSecurity: HttpSecurity): AuthenticationManager {
		val provider = DaoAuthenticationProvider()
		provider.setMessageSource(messageSource)
		provider.setPasswordEncoder(passwordEncoder)
		return ProviderManager(provider)
	}
}
