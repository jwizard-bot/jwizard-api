/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.deployment

import org.springframework.stereotype.Service
import pl.jwizard.api.network.deployment.dto.DeploymentResDto
import pl.jwizard.api.scaffold.AbstractLoggingBean

@Service
class DeploymentServiceImpl(
	val deploymentProperties: DeploymentProperties
) : DeploymentService, AbstractLoggingBean(DeploymentServiceImpl::class) {

	override fun getDeploymentProperties(): DeploymentResDto {
		val (version, lastBuildDate) = deploymentProperties
		return DeploymentResDto(version, lastBuildDate)
	}
}