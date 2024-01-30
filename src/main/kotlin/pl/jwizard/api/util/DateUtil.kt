/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.util

import org.apache.commons.lang3.time.DateUtils
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtil {
	fun nowUtcToIsoInstant(): String {
		val now = ZonedDateTime.now(ZoneOffset.UTC)
		return now.format(DateTimeFormatter.ISO_INSTANT)
	}

	fun toLocalDateTime(date: Date): LocalDateTime = Instant
		.ofEpochMilli(date.time)
		.atZone(ZoneId.of("UTC"))
		.toLocalDateTime()

	fun addMinutesToNow(minutes: Int): Date = DateUtils.addMinutes(Date(), minutes)

	fun addDaysToNow(days: Int): Date = DateUtils.addDays(Date(), days)
}
