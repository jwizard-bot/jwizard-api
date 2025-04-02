package pl.jwizard.jwa.gateway.amqp

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import pl.jwizard.jwa.gateway.amqp.receiver.CoreToApiCmdStatsReceiver
import pl.jwizard.jwa.gateway.amqp.receiver.CoreToApiListenStatsReceiver
import pl.jwizard.jwl.property.BaseEnvironment
import pl.jwizard.jwl.rabbitmq.RabbitMqServer
import pl.jwizard.jwl.rabbitmq.RabbitQueue
import pl.jwizard.jwl.rabbitmq.dto.CoreToApiCmdStatsDto
import pl.jwizard.jwl.rabbitmq.dto.CoreToApiListenStatsDto

@Component
class RabbitConfiguration {
	@Bean
	fun rabbitServer(environment: BaseEnvironment, objectMapper: ObjectMapper): RabbitMqServer {
		val rabbitServer = RabbitMqServer(objectMapper, environment)

		// queues
		rabbitServer.addQueue(RabbitQueue.JW_CORE_TO_API_CMD_STATS)
		rabbitServer.addQueue(RabbitQueue.JW_CORE_TO_API_LISTEN_STATS)

		// receivers (listeners)
		rabbitServer.addListener(CoreToApiCmdStatsDto::class, CoreToApiCmdStatsReceiver())
		rabbitServer.addListener(CoreToApiListenStatsDto::class, CoreToApiListenStatsReceiver())

		return rabbitServer
	}
}
