package io.github.tiscs.scp.messaging

import io.github.tiscs.scp.models.Event
import io.github.tiscs.scp.models.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink

@EnableBinding(value = [Sink::class])
class EventConsumer {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(EventConsumer::class.java)
    }

    @StreamListener(Sink.INPUT)
    fun consume(payload: Event<User>) {
        logger.info("Received: $payload")
    }
}
