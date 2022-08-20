package io.github.tiscs.sbp.snowflake

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("snowflake")
data class ConfigProperties(
    val enabled: Boolean? = null,
    val clusterId: Long = 0,
    val workerId: Long = 0,
    val workerSequence: String? = null,
    val clusterIdBits: Int = 5,
    val workerIdBits: Int = 5,
    val sequenceBits: Int = 12,
    val idEpoch: Long = DEFAULT_ID_EPOCH,
)
