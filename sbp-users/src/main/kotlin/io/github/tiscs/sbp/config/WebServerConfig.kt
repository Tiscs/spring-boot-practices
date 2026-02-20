package io.github.tiscs.sbp.config

import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Configuration

@Configuration
class WebServerConfig : WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    override fun customize(factory: TomcatServletWebServerFactory) {
        factory.addConnectorCustomizers({
            it.setProperty("relaxedQueryChars", "[]")
        })
    }
}
