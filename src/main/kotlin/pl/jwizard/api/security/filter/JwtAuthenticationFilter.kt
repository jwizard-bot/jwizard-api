/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import pl.jwizard.api.domain.jwtblacklist.JwtBlacklistRepository
import pl.jwizard.api.exception.app.IdentityException
import pl.jwizard.api.security.SecurityProperties
import pl.jwizard.api.security.SpringSecurityConfigurer
import pl.jwizard.api.security.UserPrincipalAuthenticationToken
import pl.jwizard.api.security.jwt.JwtService
import pl.jwizard.api.security.jwt.JwtState

@Component
class JwtAuthenticationFilter(
	private val userDetailsService: UserDetailsService,
	private val jwtService: JwtService,
	private val securityProperties: SecurityProperties,
	private val jwtBlacklistRepository: JwtBlacklistRepository,
) : AbstractFilterBean(JwtAuthenticationFilter::class) {

	private val antPathMatcher = AntPathMatcher()

	override fun doFilterInternal(
		req: HttpServletRequest,
		res: HttpServletResponse,
		chain: FilterChain
	) {
		val token = jwtService.extractAccessFromRequest(req)
		if (token.isBlank() || jwtBlacklistRepository.existsByJwt(token)) {
			chain.doFilter(req, res)
			return
		}
		val (state, claims) = jwtService.validate(token)
		if (state != JwtState.VALID && state.placeholder != null) {
			throw IdentityException.JwtGeneralException(HttpStatus.UNAUTHORIZED, state.placeholder)
		}
		var username = claims!!.subject
		if (claims.audience == securityProperties.jwtAudience.standaloneClient) {
			username = "APP_ID=$username"
		}
		val userDetails: UserDetails
		try {
			userDetails = userDetailsService.loadUserByUsername(username)
		} catch (ex: AuthenticationException) {
			chain.doFilter(req, res)
			return
		}
		SecurityContextHolder.getContext().authentication = UserPrincipalAuthenticationToken(req, userDetails)
		chain.doFilter(req, res)
	}

	override fun shouldNotFilter(req: HttpServletRequest): Boolean = SpringSecurityConfigurer
		.unsecuredMatchers
		.any { antPathMatcher.match(it, req.servletPath) }
}
