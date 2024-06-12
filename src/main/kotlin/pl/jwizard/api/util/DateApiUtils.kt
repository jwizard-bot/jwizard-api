/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.util

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateApiUtils {
	fun nowUtcToIsoInstant(): String {
		val now = ZonedDateTime.now(ZoneOffset.UTC)
		return now.format(DateTimeFormatter.ISO_INSTANT)
	}
}
