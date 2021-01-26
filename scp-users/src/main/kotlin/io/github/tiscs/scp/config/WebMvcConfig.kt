package io.github.tiscs.scp.config

import io.github.tiscs.scp.webmvc.QueryParamsResolver
import org.springframework.stereotype.Component
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class WebMvcConfig : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(QueryParamsResolver())
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/").setViewName("home")
        registry.addViewController("/login").setViewName("login")
        registry.addViewController("/logout").setViewName("logout")
        registry.addViewController("/oauth/confirm_access").setViewName("authorize")
    }
}
