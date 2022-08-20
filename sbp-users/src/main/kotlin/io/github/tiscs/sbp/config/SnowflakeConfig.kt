package io.github.tiscs.sbp.config

import io.github.tiscs.sbp.sequence.RedisSequenceProvider
import io.github.tiscs.sbp.snowflake.ConfigProperties
import io.github.tiscs.sbp.snowflake.IdWorker
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.util.Assert
import java.util.*

@Configuration
@ConditionalOnProperty("snowflake.enabled", matchIfMissing = false)
@EnableConfigurationProperties(ConfigProperties::class)
class SnowflakeConfig {
    @Bean
    fun idWorker(config: ConfigProperties, factory: Optional<RedisConnectionFactory>): IdWorker {
        val workerId = if (!config.workerSequence.isNullOrEmpty()) {
            Assert.state(factory.isPresent, "Redis connection is required when generating worker id in sequence.")
            RedisSequenceProvider(config.workerSequence, factory.get(), 0, (-1L shl config.workerIdBits).inv()).nextValue()
        } else config.workerId
        return IdWorker(config.clusterId, workerId, config.clusterIdBits, config.workerIdBits, config.sequenceBits, config.idEpoch)
    }
}
