package io.github.tiscs.scp.config

import io.github.tiscs.scp.snowflake.ConfigProperties
import io.github.tiscs.scp.snowflake.IdWorker
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ConfigProperties::class)
class SnowflakeConfig {
    @Bean
    fun idWorker(properties: ConfigProperties): IdWorker {
        return IdWorker(
            properties.clusterId,
            properties.workerId,
            properties.clusterIdBits,
            properties.workerIdBits,
            properties.sequenceBits,
            properties.idEpoch,
        )
    }
}
