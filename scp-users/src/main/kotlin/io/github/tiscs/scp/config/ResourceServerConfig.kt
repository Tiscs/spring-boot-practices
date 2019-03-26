package io.github.tiscs.scp.config

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter

@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ResourceServerConfig : ResourceServerConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.requestMatchers().antMatchers("/users/**", "/clients/**").and().csrf().disable().authorizeRequests().anyRequest().permitAll()
    }
}
