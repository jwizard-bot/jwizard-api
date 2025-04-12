package pl.jwizard.jwa.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import pl.jwizard.jwl.http.SecureHttpClientService
import pl.jwizard.jwl.i18n.I18n
import pl.jwizard.jwl.i18n.I18nInitializer
import pl.jwizard.jwl.ioc.IoCKtContextFactory
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.server.HttpServer
import pl.jwizard.jwl.server.exception.UnspecifiedExceptionAdvisor
import pl.jwizard.jwl.server.useragent.GeolocationProvider
import pl.jwizard.jwl.server.useragent.UserAgentExtractor
import java.net.http.HttpClient

@Component
internal class AppConfiguration {
	@Bean
	fun objectMapper(): ObjectMapper {
		val objectMapper = ObjectMapper()
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
		// mappers
		objectMapper.registerModule(JavaTimeModule())
		objectMapper.registerKotlinModule()
		// formatters
		objectMapper.dateFormat = StdDateFormat()
		return objectMapper
	}

	@Bean
	fun environment() = BaseEnvironment()

	@Bean
	fun messageSource(): MessageSource {
		val initializer = I18nInitializer()
		return initializer.createMessageSource()
	}

	@Bean
	fun i18n(
		messageSource: MessageSource,
		environment: BaseEnvironment,
	) = I18n(messageSource, environment)

	@Bean
	fun httpServer(
		ioCKtContextFactory: IoCKtContextFactory,
		objectMapper: ObjectMapper,
		environment: BaseEnvironment,
	) = HttpServer(ioCKtContextFactory, objectMapper, environment)

	@Bean
	fun unspecifiedExceptionAdvisor() = UnspecifiedExceptionAdvisor()

	@Bean
	fun userAgentExtractor(environment: BaseEnvironment) = UserAgentExtractor(environment)

	@Bean
	fun secureHttpClientService(
		objectMapper: ObjectMapper,
		environment: BaseEnvironment
	): SecureHttpClientService {
		val httpClient = HttpClient.newHttpClient()
		return SecureHttpClientService(httpClient, objectMapper, environment)
	}

	@Bean
	fun geolocationProvider(
		secureHttpClientService: SecureHttpClientService,
		environment: BaseEnvironment,
	) = GeolocationProvider(secureHttpClientService, environment)
}
