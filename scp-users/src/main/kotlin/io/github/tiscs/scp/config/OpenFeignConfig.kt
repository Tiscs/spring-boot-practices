package io.github.tiscs.scp.config

import feign.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails

@Configuration
class OpenFeignConfig {
    @Bean
    fun authorizationRequestInterceptor(): RequestInterceptor {
        return RequestInterceptor {
            val details = SecurityContextHolder.getContext()?.authentication?.details
            if (details is OAuth2AuthenticationDetails) {
                it.header("Authorization", "${details.tokenType} ${details.tokenValue}")
            }
        }
    }
}
