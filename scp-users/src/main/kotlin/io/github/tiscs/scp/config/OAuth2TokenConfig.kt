package io.github.tiscs.scp.config

import io.github.tiscs.scp.crypto.parseKeyPair
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore

@Configuration
@EnableConfigurationProperties(AuthorizationServerProperties::class)
class OAuth2TokenConfig(
        private val authorization: AuthorizationServerProperties
) {
    @Bean
    fun tokenStore() = JwtTokenStore(tokenConverter())

    @Bean
    fun tokenConverter(): JwtAccessTokenConverter {
        val keyValue = authorization.jwt.keyValue
        assert(keyValue != null) { "keyValue cannot be null" }
        val converter = JwtAccessTokenConverter()
        if (keyValue.startsWith("-----BEGIN")) {
            converter.setKeyPair(parseKeyPair(keyValue))
        } else {
            converter.setSigningKey(keyValue)
        }
        return converter
    }
}
