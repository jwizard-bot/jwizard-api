/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource

class UserPrincipalAuthenticationToken(
	req: HttpServletRequest,
	private val userDetails: UserDetails,
) : AbstractAuthenticationToken(userDetails.authorities) {

	init {
		details = WebAuthenticationDetailsSource().buildDetails(req)
		isAuthenticated = true
	}

	override fun getCredentials(): Any? = null
	override fun getPrincipal(): Any = userDetails
}
