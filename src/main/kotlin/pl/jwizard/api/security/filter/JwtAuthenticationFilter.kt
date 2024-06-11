/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationFilter : AbstractFilterBean(JwtAuthenticationFilter::class) {
	override fun doFilterInternal(
		req: HttpServletRequest,
		res: HttpServletResponse,
		chain: FilterChain
	) {
		chain.doFilter(req, res)
	}
}
