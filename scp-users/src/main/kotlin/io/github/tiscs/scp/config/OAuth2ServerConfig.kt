package io.github.tiscs.scp.config

import io.github.tiscs.scp.services.DbClientDetailsService
import io.github.tiscs.scp.services.DbUserDetailsService
import io.github.tiscs.scp.services.RedisAuthCodeService
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.security.oauth2.provider.token.TokenStore

@Configuration
@EnableAuthorizationServer
class OAuth2ServerConfig(
        private val userDetailsService: DbUserDetailsService,
        private val passwordEncoder: PasswordEncoder,
        private val authenticationManager: AuthenticationManager,
        private val clientDetailsService: DbClientDetailsService,
        private val authCodeService: RedisAuthCodeService,
        private val tokenStore: TokenStore,
        private val tokenEnhancer: TokenEnhancer
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
                .tokenStore(tokenStore)
                .tokenEnhancer(tokenEnhancer)
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false)
    }
}
