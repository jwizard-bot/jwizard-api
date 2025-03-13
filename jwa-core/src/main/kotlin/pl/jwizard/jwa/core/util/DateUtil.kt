package pl.jwizard.jwa.core.util

import java.time.Duration
import java.time.LocalDateTime

// convert millis to format dd:HH:mm:ss
fun convertMillisToDtf(millis: Long): String {
	val duration = Duration.ofMillis(millis)
	return "%dd, %dh, %dm, %ds".format(
		duration.toDaysPart(),
		duration.toHoursPart(),
		duration.toMinutesPart(),
		duration.toSecondsPart(),
	)
}

fun timeDifference(fromDate: LocalDateTime, toDate: LocalDateTime): Pair<String, Long> {
	val duration = Duration.between(fromDate, toDate)
	return when {
		duration.toMinutes() < 1 -> "now" to 0
		duration.toHours() < 1 -> "m" to duration.toMinutes()
		duration.toDays() < 1 -> "h" to duration.toHours()
		duration.toDays() < 30 -> "d" to duration.toDays()
		else -> "ms" to duration.toDays() / 30
	}
}
