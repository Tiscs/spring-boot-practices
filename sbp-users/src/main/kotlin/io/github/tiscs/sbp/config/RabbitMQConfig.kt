package io.github.tiscs.sbp.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    @Bean
    fun userEventQueue(): Queue = Queue("users.events", true, false, false)

    @Bean
    fun eventTopicExchange(): Exchange = TopicExchange("events.topic", true, false)

    @Bean
    fun userEventBinding(): Binding = Binding("users.events", Binding.DestinationType.QUEUE, "events.topic", "users.events", null)
}
