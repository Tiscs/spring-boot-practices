package io.github.tiscs.scp.swagger

import io.github.tiscs.scp.models.Query
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

private fun ApiFilterNames.allowableValues(): AllowableValues = AllowableListValues(this.value.toMutableList(), "string")

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
class QueryParamsPlugin : OperationBuilderPlugin {
    override fun supports(delimiter: DocumentationType?): Boolean = true

    override fun apply(context: OperationContext?) {
        if (context!!.parameters.any { p -> p.parameterType.isInstanceOf(Query::class.java) }) {
            val filterNames = context.findAnnotation(ApiFilterNames::class.java)
            context.operationBuilder().parameters(listOf(
                    ParameterBuilder().name("\$filter.name")
                            .parameterType("query")
                            .modelRef(StringModelRef)
                            .required(filterNames.isPresent && filterNames.get().required)
                            .allowableValues(if (filterNames.isPresent) filterNames.get().allowableValues() else null)
                            .build(),
                    ParameterBuilder().name("\$filter.params")
                            .parameterType("query")
                            .modelRef(StringModelRef)
                            .build(),
                    ParameterBuilder().name("\$paging.page")
                            .parameterType("query")
                            .modelRef(IntegerModelRef)
                            .defaultValue("0")
                            .build(),
                    ParameterBuilder().name("\$paging.size")
                            .parameterType("query")
                            .modelRef(IntegerModelRef)
                            .defaultValue("10")
                            .build(),
                    ParameterBuilder().name("\$orderby")
                            .parameterType("query")
                            .modelRef(StringModelRef)
                            .build(),
                    ParameterBuilder().name("\$count")
                            .parameterType("query")
                            .modelRef(BooleanModelRef)
                            .build()
            ))
        }
    }
}
