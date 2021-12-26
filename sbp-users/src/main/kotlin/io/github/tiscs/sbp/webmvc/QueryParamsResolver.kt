package io.github.tiscs.sbp.webmvc

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import io.github.tiscs.sbp.models.Filter
import io.github.tiscs.sbp.models.Paging
import io.github.tiscs.sbp.models.Query
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

const val FilterNameParameter = "\$filter.name"
const val FilterParamsParameter = "\$filter.params"
const val PagingPageParameter = "\$paging.page"
const val PagingSizeParameter = "\$paging.size"
const val OrderByParameter = "\$orderby"
const val CountOnlyParameter = "\$count"
const val DefaultPagingPage = 0
const val DefaultPagingSize = 10

private val objectMapper = ObjectMapper(YAMLFactory())

class QueryParamsResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == Query::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val filterName = webRequest.getParameter(FilterNameParameter)
        val filterParams = webRequest.getParameter(FilterParamsParameter)
        val filter = if (filterName != null) {
            Filter(
                filterName,
                if (filterParams.isNullOrEmpty()) emptyList() else objectMapper.readValue(
                    "[$filterParams]",
                    List::class.java,
                )
            )
        } else {
            null
        }
        val pagingPage = webRequest.getParameter(PagingPageParameter) ?: DefaultPagingPage.toString()
        val pagingSize = webRequest.getParameter(PagingSizeParameter) ?: DefaultPagingSize.toString()
        val orderby = webRequest.getParameter(OrderByParameter)
        val countOnly = webRequest.getParameter(CountOnlyParameter) ?: "false"
        return Query(
            Paging(pagingPage.toInt(), pagingSize.toInt()),
            filter,
            orderby,
            countOnly == "true" || countOnly == "",
        )
    }
}
