package pl.jwizard.jwa.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import nl.basjes.parse.useragent.UserAgent
import nl.basjes.parse.useragent.UserAgentAnalyzer
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import pl.jwizard.jwa.core.property.ServerProperty
import pl.jwizard.jwl.i18n.I18n
import pl.jwizard.jwl.i18n.I18nInitializer
import pl.jwizard.jwl.ioc.IoCKtContextFactory
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.server.HttpServer
import pl.jwizard.jwl.server.exception.UnspecifiedExceptionAdvisor
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
	fun httpClient(): HttpClient = HttpClient.newHttpClient()

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
	fun userAgentAnalyzer(
		environment: BaseEnvironment,
	): UserAgentAnalyzer = UserAgentAnalyzer.newBuilder()
		.withCache(environment.getProperty(ServerProperty.YAUAA_CACHE_MAX_ELEMENTS))
		.withFields(UserAgent.OPERATING_SYSTEM_NAME, UserAgent.DEVICE_CLASS)
		.build()
}
