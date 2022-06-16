package io.github.tiscs.sbp.config

import feign.Feign
import feign.Logger
import feign.slf4j.Slf4jLogger
import io.github.tiscs.sbp.clients.GitHubClient
import io.github.tiscs.sbp.openfeign.AuthorizationRequestInterceptor
import io.github.tiscs.sbp.openfeign.ConfigProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ConfigProperties::class)
class OpenFeignConfig(
    private val properties: ConfigProperties,
) {
    @Bean
    fun githubClient(): GitHubClient = Feign.builder()
        .logLevel(Logger.Level.FULL)
        .logger(Slf4jLogger(GitHubClient::class.java))
        .options(properties.toRequestOptions())
        .requestInterceptor(AuthorizationRequestInterceptor())
        .target(GitHubClient::class.java, "https://api.github.com")
}
