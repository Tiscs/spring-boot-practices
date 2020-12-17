package io.github.tiscs.scp.config

import io.github.tiscs.scp.security.RsaKeyHelper
import io.github.tiscs.scp.security.TokenClaimsParser
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore

@Configuration
@EnableConfigurationProperties(AuthorizationServerProperties::class)
class OAuth2TokenConfig(
    private val authorization: AuthorizationServerProperties,
) {
    @Bean
    fun tokenStore() = JwtTokenStore(tokenConverter())

    @Bean
    fun tokenEnhancer(): TokenEnhancer {
        val chain = TokenEnhancerChain()
        chain.setTokenEnhancers(listOf(TokenClaimsParser(), tokenConverter()))
        return chain
    }

    @Bean
    fun tokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        val keyValue = authorization.jwt.keyValue
        if (keyValue != null) {
            if (RsaKeyHelper.isPEMKeyPair(keyValue)) {
                converter.setKeyPair(RsaKeyHelper.parseKeyPair(keyValue))
            } else {
                converter.setSigningKey(keyValue)
            }
        }
        return converter
    }
}
