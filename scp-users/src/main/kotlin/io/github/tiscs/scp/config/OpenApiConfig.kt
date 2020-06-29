package io.github.tiscs.scp.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.tiscs.scp.models.Query
import io.github.tiscs.scp.openapi.QueryParamsCustomizer
import io.swagger.v3.core.jackson.ModelResolver
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.Constants.SPRINGDOC_ENABLED
import org.springdoc.core.SpringDocUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
            Info().title("SCP-Users").version("v1.0.0")
    )

    @Bean
    fun modelResolver(objectMapper: ObjectMapper) = ModelResolver(objectMapper)

    @Bean
    fun queryParamsCustomizer() = QueryParamsCustomizer()
}
