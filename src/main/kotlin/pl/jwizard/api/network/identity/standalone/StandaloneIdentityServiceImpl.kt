/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.identity.standalone

import io.jsonwebtoken.Claims
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import pl.jwizard.api.domain.jwtblacklist.JwtBlacklistDocument
import pl.jwizard.api.domain.jwtblacklist.JwtBlacklistRepository
import pl.jwizard.api.domain.refreshtoken.RefreshTokenDocument
import pl.jwizard.api.domain.refreshtoken.RefreshTokenRepository
import pl.jwizard.api.exception.app.IdentityException
import pl.jwizard.api.network.identity.standalone.dto.LoginReqDto
import pl.jwizard.api.network.identity.standalone.dto.RefreshReqDto
import pl.jwizard.api.network.identity.standalone.dto.TokenDataResDto
import pl.jwizard.api.scaffold.AbstractLoggingBean
import pl.jwizard.api.security.SecurityProperties
import pl.jwizard.api.security.jwt.JwtService
import pl.jwizard.api.util.DateUtil
import java.time.LocalDateTime

@Service
class StandaloneIdentityServiceImpl(
	private val authenticationManager: AuthenticationManager,
	private val jwtService: JwtService,
	private val securityProperties: SecurityProperties,
	private val refreshTokenRepository: RefreshTokenRepository,
	private val jwtBlacklistRepository: JwtBlacklistRepository,
) : StandaloneIdentityService, AbstractLoggingBean(StandaloneIdentityServiceImpl::class) {

	override fun login(reqDto: LoginReqDto): TokenDataResDto {
		val token = UsernamePasswordAuthenticationToken("APP_ID=${reqDto.appId}", reqDto.appSecret)
		val authentication = authenticationManager.authenticate(token)
		SecurityContextHolder.getContext().authentication = authentication

		val (access) = jwtService.generateStandaloneAppAccessToken(reqDto.appId)
		val (refresh, refreshExpiredAt) = jwtService.generateRefreshToken()

		val refreshToken = RefreshTokenDocument(
			refreshToken = refresh,
			expiredAt = DateUtil.toLocalDateTime(refreshExpiredAt),
			userDcId = reqDto.appId
		)
		refreshTokenRepository.save(refreshToken)

		log.info("Successfully login standalone app with ID: {}", reqDto.appId)
		return TokenDataResDto(
			accessToken = access,
			refreshToken = refresh,
		)
	}

	override fun refresh(reqDto: RefreshReqDto): TokenDataResDto {
		val claims = extractAppIdAndCheckClient(reqDto.expiredAccessToken)
		val appId = claims.subject
		var refresh = reqDto.refreshToken

		val refreshDocument = (refreshTokenRepository
			.findByRefreshTokenAndUserDcId(refresh, appId)
			?: throw IdentityException.RefreshTokenNotExistException(refresh, appId))

		val (access) = jwtService.generateStandaloneAppAccessToken(appId)

		if (refreshDocument.expiredAt < LocalDateTime.now()) {
			val (token, refreshExpiredAt) = jwtService.generateRefreshToken()
			refresh = token
			refreshTokenRepository.save(
				refreshDocument.copy(
					refreshToken = token,
					expiredAt = DateUtil.toLocalDateTime(refreshExpiredAt)
				)
			)
			log.info("Renew refresh token for standalone app with ID: {}", appId)
		}
		log.info("Successfully refresh standalone app session with ID: {}", appId)
		return TokenDataResDto(
			accessToken = access,
			refreshToken = refresh,
		)
	}

	override fun logout(req: HttpServletRequest) {
		val accessToken = jwtService.extractAccessFromRequest(req)
		val refreshToken = jwtService.extractRefreshFromRequest(req)

		val claims = extractAppIdAndCheckClient(accessToken)
		val appId = claims.subject

		if (!refreshTokenRepository.existsByRefreshTokenAndUserDcId(refreshToken, appId)) {
			throw IdentityException.RefreshTokenNotExistException(refreshToken, appId)
		}
		val jwtBlacklistDocument = JwtBlacklistDocument(
			jwt = accessToken,
			expiredAt = DateUtil.toLocalDateTime(claims.expiration)
		)
		jwtBlacklistRepository.save(jwtBlacklistDocument)

		refreshTokenRepository.deleteByRefreshTokenAndUserDcId(refreshToken, appId)
		SecurityContextHolder.clearContext()

		log.info("Successfully logout standalone app from session with ID: {}", appId)
	}

	private fun extractAppIdAndCheckClient(token: String): Claims {
		val claims = jwtService
			.extractClaims(token)
			.orElseThrow { IdentityException.JwtGeneralException() }

		securityProperties.standaloneClients
			.find { it.appId == claims.subject }
			?: throw IdentityException.StandaloneAppNotExistException(claims.subject)

		return claims
	}
}
