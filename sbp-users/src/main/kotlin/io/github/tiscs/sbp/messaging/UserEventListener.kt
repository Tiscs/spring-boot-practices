package io.github.tiscs.sbp.messaging

import io.github.tiscs.sbp.models.Event
import io.github.tiscs.sbp.models.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class UserEventListener {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(UserEventListener::class.java)
    }

    @RabbitListener(queues = ["users.events"])
    fun handleUserEvent(event: Event<User>) {
        LOGGER.info("Handling user event: $event")
    }
}
