package io.github.tiscs.sbp.config

import io.github.tiscs.sbp.sequence.RedisSequenceProvider
import io.github.tiscs.sbp.snowflake.ConfigProperties
import io.github.tiscs.sbp.snowflake.IdWorker
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.util.Assert
import java.util.*

@Configuration
@EnableConfigurationProperties(ConfigProperties::class)
class SnowflakeConfig {
    @Bean
    fun idWorker(config: ConfigProperties, factory: Optional<RedisConnectionFactory>): IdWorker {
        val sequence = config.workerSequence
        if (sequence != null && sequence.isNotEmpty()) {
            Assert.state(factory.isPresent, "Redis connection is required when generating worker id in sequence.")
            config.workerId = RedisSequenceProvider(sequence, factory.get(), 0, (-1L shl config.workerIdBits).inv()).nextValue()
        }
        return IdWorker(
            config.clusterId,
            config.workerId,
            config.clusterIdBits,
            config.workerIdBits,
            config.sequenceBits,
            config.idEpoch,
        )
    }
}
