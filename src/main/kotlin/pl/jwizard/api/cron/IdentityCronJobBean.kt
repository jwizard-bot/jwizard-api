/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.cron

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import pl.jwizard.api.domain.jwtblacklist.JwtBlacklistRepository
import pl.jwizard.api.scaffold.AbstractLoggingBean
import java.time.LocalDateTime

@Service
class IdentityCronJobBean(
	private val jwtBlacklistRepository: JwtBlacklistRepository
) : AbstractLoggingBean(IdentityCronJobBean::class) {

	@Scheduled(fixedRate = 24 * 60 * 60 * 1000)
	fun clearJwtContext() {
		jwtBlacklistRepository.deleteAllByExpiredAtBefore(LocalDateTime.now())
		log.info("Successfully removed unused expired JWTs from blacklist table")
	}
}
