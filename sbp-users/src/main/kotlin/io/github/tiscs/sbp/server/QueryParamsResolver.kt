package io.github.tiscs.sbp.server

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import io.github.tiscs.sbp.models.Filter
import io.github.tiscs.sbp.models.Paging
import io.github.tiscs.sbp.models.Query
import io.github.tiscs.sbp.models.Sorting
import io.github.tiscs.sbp.openapi.ApiFilters
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

const val FilterNameParameter = "\$filter.name"
const val FilterParamsParameter = "\$filter.params"
const val SortingKeysParameter = "\$sorting.keys"
const val SortingModesParameter = "\$sorting.modes"
const val PagingPageParameter = "\$paging.page"
const val PagingSizeParameter = "\$paging.size"
const val CountOnlyParameter = "\$count"
const val DefaultPagingPage = 0
const val DefaultPagingSize = 10

private val objectMapper = YAMLMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).build()
private val sortingKeysArrayType = object : TypeReference<List<String>>() {}
private val sortingModesArrayType = object : TypeReference<List<Sorting.Mode>>() {}

class QueryParamsResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == Query::class.java

    override fun resolveArgument(parameter: MethodParameter, bindingContext: BindingContext, exchange: ServerWebExchange ): Mono<Any> {
        val pagingPage = exchange.request.queryParams.getFirst(PagingPageParameter) ?: DefaultPagingPage.toString()
        val pagingSize = exchange.request.queryParams.getFirst(PagingSizeParameter) ?: DefaultPagingSize.toString()
        val paging = Paging(pagingPage.toInt(), pagingSize.toInt())

        val filterName = exchange.request.queryParams.getFirst(FilterNameParameter)
        val filterParams = exchange.request.queryParams.getFirst(FilterParamsParameter)
        val filterParamsTypes = AnnotatedElementUtils.findMergedAnnotation(parameter.method!!, ApiFilters::class.java)?.value?.singleOrNull { it.name == filterName }?.params
        val filter = if (filterName != null) {
            Filter(
                filterName,
                if (filterParams.isNullOrEmpty()) emptyList() else objectMapper.readTree(
                    "[$filterParams]"
                ).mapIndexed { i, n ->
                    objectMapper.readValue(n.toString(), filterParamsTypes?.getOrNull(i)?.java ?: Any::class.java)
                },
            )
        } else {
            null
        }

        val sortingKeys = exchange.request.queryParams.getFirst(SortingKeysParameter)
        val sortingModes = exchange.request.queryParams.getFirst(SortingModesParameter)
        val sorting = if (sortingKeys != null && sortingKeys.isNotEmpty()) {
            Sorting(
                objectMapper.readValue("[$sortingKeys]", sortingKeysArrayType),
                if (sortingModes.isNullOrEmpty()) emptyList() else objectMapper.readValue("[$sortingModes]", sortingModesArrayType),
            )
        } else {
            null
        }

        val countOnly = exchange.request.queryParams.getFirst(CountOnlyParameter) ?: "false"

        return Mono.just(Query(paging, filter, sorting, countOnly == "true" || countOnly == ""))
    }
}
