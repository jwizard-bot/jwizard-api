package pl.jwizard.jwa.core.util

import java.time.Duration

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
