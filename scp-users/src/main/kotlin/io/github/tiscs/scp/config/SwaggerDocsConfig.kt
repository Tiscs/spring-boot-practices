package io.github.tiscs.scp.config

import io.github.tiscs.scp.models.Query
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.AlternateTypeRules
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerDocsConfig {
    @Bean
    fun docket(): Docket = Docket(DocumentationType.SWAGGER_2)
            .groupName("v1.0.0")
            .useDefaultResponseMessages(false)
            .forCodeGeneration(true)
            .ignoredParameterTypes(Query::class.java)
            .alternateTypeRules(AlternateTypeRules.newRule(ResponseEntity::class.java, Void::class.java))
            .apiInfo(ApiInfoBuilder()
                    .title("SCP-Users")
                    .version("v1.0.0")
                    .build())
            .select()
            .apis(RequestHandlerSelectors.basePackage("io.github.tiscs.scp.controllers"))
            .paths(PathSelectors.any())
            .build()
}
