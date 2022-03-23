package io.github.tiscs.sbp.messaging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class UserEventListener {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(UserEventListener::class.java)
    }

    @RabbitListener(queues = ["users.events"])
    fun handleUserEvent(message: String) {
        LOGGER.info("Handling user event: $message")
    }
}
