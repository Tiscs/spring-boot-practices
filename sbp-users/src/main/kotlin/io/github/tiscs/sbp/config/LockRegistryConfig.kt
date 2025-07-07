package io.github.tiscs.sbp.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.integration.redis.util.RedisLockRegistry

@Configuration
class LockRegistryConfig(
    @Value($$"${spring.application.name}")
    private val registryKey: String,
) {
    @Bean
    fun redisLockRegistry(
        connectionFactory: RedisConnectionFactory,
    ): RedisLockRegistry {
        return RedisLockRegistry(connectionFactory, "LOCK_REGISTRY.$registryKey")
    }
}
