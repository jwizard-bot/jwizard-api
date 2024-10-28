/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.jwa.core.property

import org.springframework.stereotype.Component
import pl.jwizard.jwl.SpringKtContextFactory
import pl.jwizard.jwl.property.BaseEnvironment

/**
 * A component that manages the application's environment properties.
 *
 * This class extends [BaseEnvironment] to provide functionality for loading and accessing properties in the context
 * of a Spring application. It utilizes the [SpringKtContextFactory] to retrieve beans and manage property sources.
 *
 * @property springKtContextFactory Provides access to the Spring context for retrieving beans.
 * @author Miłosz Gilga
 */
@Component
class EnvironmentBean(
	private val springKtContextFactory: SpringKtContextFactory,
) : BaseEnvironment(springKtContextFactory)
