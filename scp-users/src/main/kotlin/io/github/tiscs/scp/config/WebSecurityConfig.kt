package io.github.tiscs.scp.config

import io.github.tiscs.scp.services.DbUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class WebSecurityConfig(
        @Autowired private val userDetailsService: DbUserDetailsService
) : WebSecurityConfigurerAdapter() {
    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity) {
        http.csrf()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll()
                .and().authorizeRequests().antMatchers("/", "/webjars/**", "/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**").permitAll()
                .and().authorizeRequests().anyRequest().authenticated()

    }
}
