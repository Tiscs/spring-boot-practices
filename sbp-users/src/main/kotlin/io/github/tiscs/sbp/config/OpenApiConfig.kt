package io.github.tiscs.sbp.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.tiscs.sbp.models.Query
import io.github.tiscs.sbp.openapi.QueryParamsCustomizer
import io.github.tiscs.sbp.security.SecuritySchemeKeys
import io.swagger.v3.core.jackson.ModelResolver
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.utils.Constants.SPRINGDOC_ENABLED
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
@ConditionalOnProperty(name = [SPRINGDOC_ENABLED], matchIfMissing = true)
class OpenApiConfig {
    companion object {
        init {
            SpringDocUtils.getConfig()
                .addRequestWrapperToIgnore(Query::class.java)
                .replaceWithClass(ObjectNode::class.java, Map::class.java)
                .replaceWithClass(ArrayNode::class.java, List::class.java)
        }
    }

    @Bean
    fun openApi(): OpenAPI = OpenAPI().info(
        Info().title("SBP-Users").version("v1.0.0")
    ).components(Components()
        .addSecuritySchemes(SecuritySchemeKeys.CLIENT_BASIC, SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"))
        .addSecuritySchemes(SecuritySchemeKeys.BEARER_TOKEN, SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
    )


    @Bean
    fun modelResolver(objectMapper: ObjectMapper) = ModelResolver(objectMapper)

    @Bean
    fun queryParamsCustomizer() = QueryParamsCustomizer()

    @Bean
    fun openApiRouter() = router {
        GET("/scalar.html") {
            ServerResponse.ok().render("scalar")
        }
        GET("/swagger.html") {
            ServerResponse.ok().render("swagger")
        }
        GET("/api-docs.html") {
            ServerResponse.ok().render("api-docs")
        }
    }
}
