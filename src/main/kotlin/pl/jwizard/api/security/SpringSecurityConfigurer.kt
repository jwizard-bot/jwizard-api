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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
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
	private val securityProperties: SecurityProperties,
	private val middlewareExceptionFilter: MiddlewareExceptionFilter,
	private val securityChainResolver: SecurityChainResolver,
	private val accessDeniedResolver: AccessDeniedResolver,
	private val jwtAuthenticationFilter: JwtAuthenticationFilter,
	private val passwordEncoder: PasswordEncoder,
	private val messageSource: MessageSource,
) : AbstractLoggingBean(SpringSecurityConfigurer::class) {

	companion object {
		const val STANDALONE_CLIENT = "STANDALONE_CLIENT"
		const val USER = "USER"
		val unsecuredMatchers = arrayOf(
			"/api/v1/identity/standalone/login",
			"/api/v1/identity/standalone/refresh",
		)
		val standaloneSecuredMatchers = arrayOf(
			"/api/v1/command/all",
			"/api/v1/guild/standalone/{guildId}",
			"/api/v1/guild/standalone/settings/music-channel/guild/{guildId}",
		)
	}

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
					.requestMatchers(*unsecuredMatchers).permitAll()
					.requestMatchers(*standaloneSecuredMatchers).hasRole(STANDALONE_CLIENT)
					.anyRequest().authenticated()
			}
			.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
		return httpSecurity.build()
	}

	@Bean
	fun authenticationManager(
		httpSecurity: HttpSecurity,
		statelessUserDetailsService: StatelessUserDetailsService,
	): AuthenticationManager {
		val provider = DaoAuthenticationProvider()
		provider.setMessageSource(messageSource)
		provider.setPasswordEncoder(passwordEncoder)
		provider.setUserDetailsService(statelessUserDetailsService)

		val builder = httpSecurity.getSharedObject(AuthenticationManagerBuilder::class.java)
		builder.authenticationProvider(provider)

		if (securityProperties.standaloneClients.isEmpty()) {
			log.warn("WARNING! Not found any standalone clients configuration.")
		}
		securityProperties.standaloneClients
			.map { StandaloneClient(it.appId, passwordEncoder.encode(it.appSecret)) }
			.map { User.builder().username(it.appId).password(it.appSecret).roles(USER, STANDALONE_CLIENT) }
			.map { it.build() }
			.forEach {
				builder.inMemoryAuthentication().withUser(it)
				log.info("Successfully loaded standalone app with ID: {}", it.username)
			}
		return ProviderManager(provider)
	}
}
