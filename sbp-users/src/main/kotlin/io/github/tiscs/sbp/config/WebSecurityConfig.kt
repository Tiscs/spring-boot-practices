package io.github.tiscs.sbp.config

import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
    /**
     * @see org.springframework.security.converter.RsaKeyConverters
     */
    @Value("\${spring.security.oauth2.authorization.jwt.private-key-value}")
    private val jwtPrivateKey: RSAPrivateKey,
    @Value("\${spring.security.oauth2.resourceserver.jwt.public-key-value}")
    private val jwtPublicKey: RSAPublicKey,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        return JWKSource<SecurityContext> { selector: JWKSelector, _: SecurityContext? ->
            selector.select(JWKSet(RSAKey.Builder(jwtPublicKey).privateKey(jwtPrivateKey).build()))
        }
    }

    @Bean
    fun jwtEncoder(): JwtEncoder = NimbusJwtEncoder(jwkSource())

    @Bean
    fun jwtDecoder(): JwtDecoder = NimbusJwtDecoder.withPublicKey(jwtPublicKey).build()

    @Bean
    fun jwtFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
            csrf { disable() }
            logout { disable() }
            oauth2ResourceServer {
                jwt { }
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
        }
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return InMemoryUserDetailsManager(
            User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("ADMIN_USER").build()
        )
    }
}
