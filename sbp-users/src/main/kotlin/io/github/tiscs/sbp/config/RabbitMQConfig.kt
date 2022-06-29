package io.github.tiscs.sbp.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig(
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun messageConverter(): MessageConverter = Jackson2JsonMessageConverter(objectMapper)

    @Bean
    fun userEventQueue(): Queue = Queue("users.events", true, false, false)

    @Bean
    fun delayedUserEventQueue(): Queue = Queue("users.events.delayed", true, false, false, mapOf("x-dead-letter-exchange" to "events.topic", "x-dead-letter-routing-key" to "users.events"))

    @Bean
    fun eventTopicExchange(): Exchange = TopicExchange("events.topic", true, false)

    @Bean
    fun userEventBinding(): Binding = Binding("users.events", Binding.DestinationType.QUEUE, "events.topic", "users.events", null)
}
