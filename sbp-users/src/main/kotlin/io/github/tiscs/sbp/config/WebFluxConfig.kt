package io.github.tiscs.sbp.config

import io.github.tiscs.sbp.server.QueryParamsResolver
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer

@Component
class WebFluxConfig : WebFluxConfigurer {
    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        configurer.addCustomResolver(QueryParamsResolver())
    }

    @Bean
    fun viewsRouter() = router {
        GET("/") {
            ServerResponse.ok().render("home")
        }
    }
}
