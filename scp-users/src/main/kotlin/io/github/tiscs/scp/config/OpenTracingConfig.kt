package io.github.tiscs.scp.config

import io.opentracing.Tracer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(prefix = "opentracing.jaeger", name = ["enabled"], havingValue = "false")
class OpenTracingConfig {
    @Bean
    fun tracer(): Tracer = io.opentracing.noop.NoopTracerFactory.create()
}