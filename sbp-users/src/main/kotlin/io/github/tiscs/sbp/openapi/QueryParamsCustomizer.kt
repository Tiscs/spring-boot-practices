package io.github.tiscs.sbp.openapi

import io.github.tiscs.sbp.models.Query
import io.github.tiscs.sbp.server.*
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.media.BooleanSchema
import io.swagger.v3.oas.models.media.IntegerSchema
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.QueryParameter
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.web.method.HandlerMethod

class QueryParamsCustomizer : OperationCustomizer {
    override fun customize(operation: Operation, method: HandlerMethod): Operation {
        if (method.methodParameters.any { it.parameterType == Query::class.java }) {
            val filters = method.getMethodAnnotation(ApiFilters::class.java)
            operation
                .addParametersItem(
                    QueryParameter().name(FilterNameParameter)
                        .schema(StringSchema()._enum(filters?.value?.map { it.name }))
                        .required(filters?.required ?: false)
                )
                .addParametersItem(
                    QueryParameter().name(FilterParamsParameter)
                        .examples(filters?.value?.associate {
                            it.name to Example()
                                .value(it.example.ifEmpty { null })
                                .description(it.description.ifEmpty { null })
                        })
                        .schema(StringSchema())
                )
                .addParametersItem(
                    QueryParameter().name(PagingPageParameter)
                        .schema(IntegerSchema()._default(DefaultPagingPage))
                )
                .addParametersItem(
                    QueryParameter().name(PagingSizeParameter)
                        .schema(IntegerSchema()._default(DefaultPagingSize))
                )
                .addParametersItem(
                    QueryParameter().name(SortingKeysParameter)
                        .schema(StringSchema()._default(null))
                )
                .addParametersItem(
                    QueryParameter().name(SortingModesParameter)
                        .schema(StringSchema()._default(null))
                )
                .addParametersItem(
                    QueryParameter().name(CountOnlyParameter)
                        .schema(BooleanSchema()._default(false))
                )
        }
        return operation
    }
}
