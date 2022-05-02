package io.github.tiscs.sbp.webmvc

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
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

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

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val pagingPage = webRequest.getParameter(PagingPageParameter) ?: DefaultPagingPage.toString()
        val pagingSize = webRequest.getParameter(PagingSizeParameter) ?: DefaultPagingSize.toString()
        val paging = Paging(pagingPage.toInt(), pagingSize.toInt())

        val filterName = webRequest.getParameter(FilterNameParameter)
        val filterParams = webRequest.getParameter(FilterParamsParameter)
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

        val sortingKeys = webRequest.getParameter(SortingKeysParameter)
        val sortingModes = webRequest.getParameter(SortingModesParameter)
        val sorting = if (sortingKeys != null && sortingKeys.isNotEmpty()) {
            Sorting(
                objectMapper.readValue("[$sortingKeys]", sortingKeysArrayType),
                if (sortingModes.isNullOrEmpty()) emptyList() else objectMapper.readValue(
                    "[$sortingModes]", sortingModesArrayType
                ),
            )
        } else {
            null
        }

        val countOnly = webRequest.getParameter(CountOnlyParameter) ?: "false"

        return Query(
            paging,
            filter,
            sorting,
            countOnly == "true" || countOnly == "",
        )
    }
}
