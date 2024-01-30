/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.identity.standalone

import pl.jwizard.api.network.identity.standalone.dto.LoginReqDto
import pl.jwizard.api.network.identity.standalone.dto.RefreshReqDto
import pl.jwizard.api.network.identity.standalone.dto.TokenDataResDto

interface StandaloneIdentityService {
	fun login(reqDto: LoginReqDto): TokenDataResDto
	fun refresh(reqDto: RefreshReqDto): TokenDataResDto
}
