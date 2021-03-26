package io.github.tiscs.scp.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(prefix = "opentracing.jaeger", name = ["enabled"], havingValue = "false")
class OpenTracingConfig {
    @Bean
    fun tracer(): io.opentracing.Tracer = io.opentracing.noop.NoopTracerFactory.create()
}
