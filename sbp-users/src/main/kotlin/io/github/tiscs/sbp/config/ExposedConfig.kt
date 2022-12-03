package io.github.tiscs.sbp.config

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

// https://github.com/JetBrains/Exposed/issues/1636
@Import(value = [ExposedAutoConfiguration::class])
@EnableAutoConfiguration(exclude = [DataSourceTransactionManagerAutoConfiguration::class])
@Configuration
class ExposedConfig
