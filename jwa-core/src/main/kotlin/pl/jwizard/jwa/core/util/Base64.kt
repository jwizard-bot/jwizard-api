package pl.jwizard.jwa.core.util

import java.util.*

fun base64encode(raw: ByteArray): String = Base64.getEncoder().encodeToString(raw)

fun base64decode(encoded: String): ByteArray = Base64.getDecoder().decode(encoded)
