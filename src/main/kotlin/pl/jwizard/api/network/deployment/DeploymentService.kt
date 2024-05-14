/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.deployment

import pl.jwizard.api.network.deployment.dto.DeploymentResDto

interface DeploymentService {
	fun getDeploymentProperties(): DeploymentResDto
}