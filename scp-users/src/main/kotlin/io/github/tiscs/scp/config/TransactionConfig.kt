package io.github.tiscs.scp.config

import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class TransactionConfig {
    @Bean
    fun transactionManager(dataSource: DataSource) = SpringTransactionManager(dataSource)
}
