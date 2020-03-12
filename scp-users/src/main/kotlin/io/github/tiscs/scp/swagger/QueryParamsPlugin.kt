package io.github.tiscs.scp.swagger

import io.github.tiscs.scp.models.Query
import io.github.tiscs.scp.webmvc.*
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.AllowableListValues
import springfox.documentation.service.AllowableValues
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.OperationBuilderPlugin
import springfox.documentation.spi.service.contexts.OperationContext
import springfox.documentation.swagger.common.SwaggerPluginSupport

private val StringModelRef = ModelRef("string")
private val BooleanModelRef = ModelRef("boolean")
private val IntegerModelRef = ModelRef("int")

private fun ApiFilters.allowableValues(): AllowableValues = AllowableListValues(this.value.map { it.name }, "string")
private fun ApiFilters.getDescriptions(): List<String> = this.value.map { "**${it.name}**: *`${it.description}`*" }

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
class QueryParamsPlugin : OperationBuilderPlugin {
    override fun supports(delimiter: DocumentationType?): Boolean = true

    override fun apply(context: OperationContext?) {
        if (context!!.parameters.any { p -> p.parameterType.isInstanceOf(Query::class.java) }) {
            val filters = context.findAllAnnotations(ApiFilters::class.java).firstOrNull()
            context.operationBuilder().parameters(listOf(
                    ParameterBuilder().name(FilterNameParameter)
                            .description("Filter name.")
                            .parameterType("query")
                            .modelRef(StringModelRef)
                            .required(filters?.required ?: false)
                            .allowableValues(filters?.allowableValues())
                            .build(),
                    ParameterBuilder().name(FilterParamsParameter)
                            .description(filters?.getDescriptions()?.joinToString("\n", "Filter params.\n") ?: "Filter params.")
                            .parameterType("query")
                            .modelRef(StringModelRef)
                            .build(),
                    ParameterBuilder().name(PagingPageParameter)
                            .description("Page index.")
                            .parameterType("query")
                            .modelRef(IntegerModelRef)
                            .defaultValue("0")
                            .build(),
                    ParameterBuilder().name(PagingSizeParameter)
                            .description("Page size.")
                            .parameterType("query")
                            .modelRef(IntegerModelRef)
                            .defaultValue("10")
                            .build(),
                    ParameterBuilder().name(OrderByParameter)
                            .description("Sort expression.")
                            .parameterType("query")
                            .modelRef(StringModelRef)
                            .build(),
                    ParameterBuilder().name(CountOnlyParameter)
                            .description("Return count only.")
                            .parameterType("query")
                            .modelRef(BooleanModelRef)
                            .defaultValue("false")
                            .build()
            ))
        }
    }
}
