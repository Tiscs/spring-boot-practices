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
                            .description("过滤器名称。")
                            .parameterType("query")
                            .modelRef(StringModelRef)
                            .required(filters?.required ?: false)
                            .allowableValues(filters?.allowableValues())
                            .build(),
                    ParameterBuilder().name(FilterParamsParameter)
                            .description(filters?.getDescriptions()?.joinToString("\n", "过滤器参数。\n") ?: "过滤器参数。")
                            .parameterType("query")
                            .modelRef(StringModelRef)
                            .build(),
                    ParameterBuilder().name(PagingPageParameter)
                            .description("分页页码。")
                            .parameterType("query")
                            .modelRef(IntegerModelRef)
                            .defaultValue("0")
                            .build(),
                    ParameterBuilder().name(PagingSizeParameter)
                            .description("分页大小。")
                            .parameterType("query")
                            .modelRef(IntegerModelRef)
                            .defaultValue("10")
                            .build(),
                    ParameterBuilder().name(OrderByParameter)
                            .description("排序方式。")
                            .parameterType("query")
                            .modelRef(StringModelRef)
                            .build(),
                    ParameterBuilder().name(CountOnlyParameter)
                            .description("只返回条数。")
                            .parameterType("query")
                            .modelRef(BooleanModelRef)
                            .defaultValue("false")
                            .build()
            ))
        }
    }
}
