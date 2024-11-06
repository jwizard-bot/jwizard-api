/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.property

import org.springframework.stereotype.Component
import pl.jwizard.jwl.IoCKtContextFactory
import pl.jwizard.jwl.property.BaseEnvironment

/**
 * Component class for managing and retrieving application properties for specific guilds, using remote sources such as
 * a database. Extends [BaseEnvironment] to utilize centralized environment configuration and property handling.
 *
 * @property ioCKtContextFactory Provides access to the IoC context for retrieving beans.
 * @author Miłosz Gilga
 */
@Component
class EnvironmentBean(
	private val ioCKtContextFactory: IoCKtContextFactory,
) : BaseEnvironment(ioCKtContextFactory)
