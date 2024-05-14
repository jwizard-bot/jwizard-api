/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.network.deployment

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.jwizard.api.network.deployment.dto.DeploymentResDto

@RestController
@RequestMapping("/api/v1/deployment")
class DeploymentController(
	val deploymentService: DeploymentService
) {
	@GetMapping
	fun getDeploymentProperties(): ResponseEntity<DeploymentResDto> {
		return ResponseEntity.ok(deploymentService.getDeploymentProperties())
	}
}