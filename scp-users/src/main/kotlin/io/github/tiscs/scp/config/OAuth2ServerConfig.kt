package io.github.tiscs.scp.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

@Configuration
@EnableAuthorizationServer
class OAuth2ServerConfig
@Autowired
constructor(
        private val passwordEncoder: PasswordEncoder,
        private val authenticationManager: AuthenticationManager
) : AuthorizationServerConfigurer {

    @Bean
    fun tokenStore(): TokenStore {
        return InMemoryTokenStore()
    }

    @Throws(Exception::class)
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security.tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder)
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient("CGtU4ayx9xUR")
                .secret("{noop}IzFS77pjBZoQD3eS5WDl4Hu6")
                .scopes("OPENID")
                .authorizedGrantTypes("authorization_code", "refresh_token", "password", "implicit", "client_credentials")
                .redirectUris("https://github.com/tiscs/spring-cloud-practices")
    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .reuseRefreshTokens(false)
    }
}
