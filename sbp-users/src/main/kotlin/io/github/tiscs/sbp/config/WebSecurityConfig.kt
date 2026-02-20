package io.github.tiscs.sbp.config

import com.nimbusds.jose.jwk.RSAKey
import io.github.tiscs.sbp.security.SingletonJWKSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.NullRequestCache
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
    fun jwtDecoder(): JwtDecoder = NimbusJwtDecoder.withPublicKey(jwtPublicKey).build()

    @Bean
    fun formFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher("/login", "/logout", "/oauth/authorize")
            authorizeHttpRequests {
                authorize("/login", permitAll)
                authorize("/logout", authenticated)
                authorize("/oauth/authorize", authenticated)
            }
            formLogin {
                loginPage = "/login"
            }
            logout {
                logoutUrl = "/logout"
            }
            requestCache {
                requestCache = HttpSessionRequestCache()
            }
        }
        return http.build()
    }

    @Bean
    fun jwtFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            oauth2ResourceServer {
                jwt {}
            }
            logout {
                disable()
            }
            csrf {
                disable()
            }
            requestCache {
                requestCache = NullRequestCache()
            }
        }
        return http.build()
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
