/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.security.filter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.filter.OncePerRequestFilter
import kotlin.reflect.KClass

abstract class AbstractFilterBean(clazz: KClass<*>) : OncePerRequestFilter() {
	protected val log: Logger = LoggerFactory.getLogger(clazz.java)
}
