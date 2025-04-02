package pl.jwizard.jwa.gateway.amqp.receiver

import org.springframework.amqp.core.Message
import pl.jwizard.jwl.rabbitmq.RabbitMqReceiver
import pl.jwizard.jwl.rabbitmq.RabbitQueue
import pl.jwizard.jwl.rabbitmq.dto.CoreToApiListenStatsDto

class CoreToApiListenStatsReceiver : RabbitMqReceiver<CoreToApiListenStatsDto>() {
	override val queue = RabbitQueue.JW_CORE_TO_API_CMD_STATS

	override fun onMessage(message: Message, payload: CoreToApiListenStatsDto) {
		// TODO
	}
}
