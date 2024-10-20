/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core

import org.springframework.context.annotation.ComponentScan

/**
 * Apply this annotation on main class, where you invoke `run` static method from [ServerAppRunner] class.
 *
 * @author Miłosz Gilga
 */
@ComponentScan(basePackages = [ServerAppRunner.BASE_PACKAGE])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ServerApp
