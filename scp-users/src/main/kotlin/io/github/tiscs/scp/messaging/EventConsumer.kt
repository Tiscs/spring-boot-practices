package io.github.tiscs.scp.messaging

import io.github.tiscs.scp.models.Event
import io.github.tiscs.scp.models.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import java.util.concurrent.atomic.AtomicInteger

@EnableBinding(value = [Sink::class])
class EventConsumer {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventConsumer::class.java)
    }

    @StreamListener(Sink.INPUT)
    fun consume(@Payload payload: Event<User>, @Header("deliveryAttempt") da: AtomicInteger) {
        logger.info("Received: $payload")
    }
}
