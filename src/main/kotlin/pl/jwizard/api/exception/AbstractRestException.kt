/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import pl.jwizard.api.i18n.ILocaleSet
import kotlin.reflect.KClass

abstract class AbstractRestException(
	val httpStatus: HttpStatus,
	val placeholder: ILocaleSet,
	clazz: KClass<*>,
	logMessage: String,
	val variables: Map<String, Any>
) : RuntimeException(placeholder.getPlaceholder()) {

	constructor(
		httpStatus: HttpStatus,
		placeholder: ILocaleSet,
		clazz: KClass<*>,
		logMessage: String,
	) : this(httpStatus, placeholder, clazz, logMessage, mapOf())

	constructor(
		httpStatus: HttpStatus,
		placeholder: ILocaleSet,
	) : this(httpStatus, placeholder, AbstractRestException::class, "", mapOf())

	init {
		if (logMessage.isNotBlank()) {
			LoggerFactory.getLogger(clazz.java).error(logMessage)
		}
	}
}
