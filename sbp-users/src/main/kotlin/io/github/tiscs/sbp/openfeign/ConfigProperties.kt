package io.github.tiscs.sbp.openfeign

import feign.Request
import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.concurrent.TimeUnit

@ConfigurationProperties("openfeign")
class ConfigProperties {
    var connectTimeout: Long = 10
    var readTimeout: Long = 60
    var followRedirects: Boolean = true

    fun toRequestOptions(): Request.Options {
        return Request.Options(connectTimeout, TimeUnit.SECONDS, readTimeout, TimeUnit.SECONDS, followRedirects)
    }
}
