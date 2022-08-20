package io.github.tiscs.sbp.openfeign

import feign.Request
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.util.concurrent.TimeUnit

@ConstructorBinding
@ConfigurationProperties("openfeign")
data class ConfigProperties(
    val connectTimeout: Long = 10,
    val readTimeout: Long = 60,
    val followRedirects: Boolean = true,
) {
    fun toRequestOptions(): Request.Options {
        return Request.Options(connectTimeout, TimeUnit.SECONDS, readTimeout, TimeUnit.SECONDS, followRedirects)
    }
}
