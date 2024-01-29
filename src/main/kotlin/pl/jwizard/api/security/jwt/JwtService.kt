/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import pl.jwizard.api.scaffold.AbstractLoggingBean
import pl.jwizard.api.security.SecurityProperties
import pl.jwizard.api.util.DateUtil
import java.security.Key
import java.security.SignatureException
import java.util.*

@Service
class JwtService(
	private val securityProperties: SecurityProperties,
) : AbstractLoggingBean(JwtService::class) {

	companion object {
		const val BEARER_PREFIX = "Bearer "
		const val X_REFRESH_TOKEN = "X-RefreshToken"
	}

	fun generateStandaloneAppAccessToken(appId: String): TokenData {
		val expiredAt = DateUtil.addMinutesToNow(securityProperties.life.accessMinutes)
		return TokenData(
			token = generateBaseToken(appId, securityProperties.jwtAudience.standaloneClient, expiredAt).compact(),
			expiredAt,
		)
	}

	fun generateRefreshToken(): TokenData = TokenData(
		token = UUID.randomUUID().toString(),
		expiredAt = DateUtil.addDaysToNow(securityProperties.life.refreshDays),
	)

	fun validate(token: String): JwtValidateState {
		var state = JwtState.VALID
		var claims: Claims? = null
		try {
			claims = Jwts.parserBuilder()
				.setSigningKey(getSignedKey())
				.build()
				.parseClaimsJws(token)
				.body
		} catch (ex: ExpiredJwtException) {
			log.error("Passed token is expired. Details: {}", ex.message)
			state = JwtState.EXPIRED
		} catch (ex: SignatureException) {
			log.error("Passed token signature is invalid. Cause: {}", ex.message)
			state = JwtState.INVALID
		} catch (ex: RuntimeException) {
			log.error("Passed token is malformed or corrupted. Cause: {}", ex.message)
			state = JwtState.INVALID
		}
		return JwtValidateState(state, claims)
	}

	fun extractAccessFromRequest(req: HttpServletRequest): String {
		val bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION)
		if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length)
		}
		return ""
	}

	fun extractRefreshFromRequest(req: HttpServletRequest): String {
		val refreshToken = req.getHeader(X_REFRESH_TOKEN)
		if (refreshToken != null) {
			return refreshToken
		}
		return ""
	}

	fun extractClaims(token: String): Optional<Claims> = Optional.ofNullable(validate(token).claims)

	private fun generateBaseToken(
		subject: String,
		claims: Claims,
		audience: String,
		expiredAt: Date,
	): JwtBuilder = Jwts.builder()
		.signWith(getSignedKey(), SignatureAlgorithm.HS256)
		.setIssuedAt(Date(System.currentTimeMillis()))
		.setClaims(claims)
		.setSubject(subject)
		.setIssuer(securityProperties.jwtIssuer)
		.setAudience(audience)
		.setExpiration(expiredAt)

	private fun generateBaseToken(
		subject: String,
		audience: String,
		expiredAt: Date,
	) = generateBaseToken(subject, Jwts.claims(), audience, expiredAt)

	private fun getSignedKey(): Key {
		val keyBytes = Decoders.BASE64.decode(securityProperties.jwtSecret)
		return Keys.hmacShaKeyFor(keyBytes)
	}
}
