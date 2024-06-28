/*
 * Copyright (c) 2024 by JWizard
 * Originally developed by Miłosz Gilga <https://miloszgilga.pl>
 */
package pl.jwizard.api.config

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class BeansInstantiator {
	@Bean
	fun modelMapper(): ModelMapper {
		val modelMapper = ModelMapper()
		modelMapper.configuration
			.setFieldMatchingEnabled(true)
			.setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
			.setMatchingStrategy(MatchingStrategies.STRICT)
		return modelMapper
	}

	@Bean
	fun restTemplate(): RestTemplate = RestTemplate()
}
