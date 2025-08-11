package io.github.tiscs.sbp.config

import com.nimbusds.jose.jwk.RSAKey
import io.github.tiscs.sbp.security.SingletonJWKSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(
    /**
     * @see org.springframework.security.converter.RsaKeyConverters
     */
    @param:Value($$"${spring.security.oauth2.authorization.jwt.private-key-value}")
    private val jwtPrivateKey: RSAPrivateKey,
    @param:Value($$"${spring.security.oauth2.resourceserver.jwt.public-key-value}")
    private val jwtPublicKey: RSAPublicKey,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun jwtEncoder(): JwtEncoder = NimbusJwtEncoder(
        SingletonJWKSource(
            RSAKey.Builder(jwtPublicKey).privateKey(jwtPrivateKey).build()
        )
    )

    @Bean
    fun jwtDecoder(): ReactiveJwtDecoder = NimbusReactiveJwtDecoder.withPublicKey(jwtPublicKey).build()

    @Bean
    fun formFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            securityMatcher(ServerWebExchangeMatchers.pathMatchers("/login", "/logout", "/oauth/authorize"))
            authorizeExchange {
                authorize(ServerWebExchangeMatchers.pathMatchers("/login"), permitAll)
                authorize(ServerWebExchangeMatchers.pathMatchers("/logout", "/oauth/authorize"), authenticated)
            }
            formLogin {
                loginPage = "/login"
            }
            logout {
                logoutUrl = "/logout"
            }
            requestCache {
                requestCache = WebSessionServerRequestCache()
            }
        }
    }

    @Bean
    fun jwtFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        return http {
            authorizeExchange {
                authorize(anyExchange, permitAll)
            }
            oauth2ResourceServer { jwt {} }
            logout { disable() }
            csrf { disable() }
            requestCache {
                requestCache = NoOpServerRequestCache.getInstance()
            }
        }
    }

    @Bean
    fun securityRouter() = router {
        GET("/login") {
            ServerResponse.ok().render("login")
        }
        GET("/logout") {
            ServerResponse.ok().render("logout")
        }
        GET("/oauth/authorize") {
            ServerResponse.ok().render("authorize")
        }
    }
}
