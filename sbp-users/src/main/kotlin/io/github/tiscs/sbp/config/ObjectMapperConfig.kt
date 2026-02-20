package io.github.tiscs.sbp.config

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.cfg.DateTimeFeature

@Configuration
class ObjectMapperConfig {

    @Bean
    fun jsonMapperBuilderCustomizer(): JsonMapperBuilderCustomizer {
        return JsonMapperBuilderCustomizer { builder ->
            builder
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }
}
