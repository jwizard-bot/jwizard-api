/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.identity.standalone

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.jwizard.api.network.identity.standalone.dto.LoginReqDto
import pl.jwizard.api.network.identity.standalone.dto.RefreshReqDto
import pl.jwizard.api.network.identity.standalone.dto.TokenDataResDto

@RestController
@RequestMapping("/api/v1/identity/standalone")
class StandaloneIdentityController(
	private val identityService: StandaloneIdentityService
) {
	@PostMapping("/login")
	fun login(
		@Valid @RequestBody reqDto: LoginReqDto
	): ResponseEntity<TokenDataResDto> = ResponseEntity.ok(identityService.login(reqDto))

	@PatchMapping("/refresh")
	fun refresh(
		@Valid @RequestBody reqDto: RefreshReqDto
	): ResponseEntity<TokenDataResDto> = ResponseEntity.ok(identityService.refresh(reqDto))

	@DeleteMapping("/logout")
	fun logout(
		req: HttpServletRequest,
	): ResponseEntity<Unit> {
		identityService.logout(req)
		return ResponseEntity(HttpStatus.NO_CONTENT)
	}
}
