package io.github.tiscs.scp.config

import io.github.tiscs.scp.services.DbClientDetailsService
import io.github.tiscs.scp.services.RedisAuthCodeService
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.AccessTokenConverter
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

@Configuration
@EnableAuthorizationServer
class OAuth2ServerConfig(
        private val passwordEncoder: PasswordEncoder,
        private val authenticationManager: AuthenticationManager,
        private val clientDetailsService: DbClientDetailsService,
        private val authCodeService: RedisAuthCodeService,
        private val tokenStore: ObjectProvider<TokenStore>,
        private val tokenConverter: ObjectProvider<AccessTokenConverter>
) : AuthorizationServerConfigurer {
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security.tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients()
                .passwordEncoder(passwordEncoder)
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.withClientDetails(clientDetailsService)
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authenticationManager(authenticationManager)
                .authorizationCodeServices(authCodeService)
                .tokenStore(tokenStore.getIfAvailable { InMemoryTokenStore() })
                .accessTokenConverter(tokenConverter.getIfAvailable { null })
                .reuseRefreshTokens(false)
    }
}
