package io.github.tiscs.scp.webmvc

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import io.github.tiscs.scp.models.Filter
import io.github.tiscs.scp.models.Paging
import io.github.tiscs.scp.models.Query
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

private val ObjectMapper = ObjectMapper(YAMLFactory())

class QueryParamsResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == Query::class.java

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        val filter: Filter?
        val filterName = webRequest.getParameter(FilterNameParameter)
        if (filterName != null) {
            val filterParams = webRequest.getParameter(FilterParamsParameter)
            filter = Filter(filterName, if (filterParams.isNullOrEmpty()) listOf() else ObjectMapper.readValue("[$filterParams]", List::class.java))
        } else {
            filter = null
        }
        val pagingPage = webRequest.getParameter(PagingPageParameter) ?: "0"
        val pagingSize = webRequest.getParameter(PagingSizeParameter) ?: "10"
        val orderby = webRequest.getParameter(OrderByParameter)
        val countOnly = webRequest.getParameter(CountOnlyParameter) ?: "false"
        return Query(Paging(pagingPage.toInt(), pagingSize.toInt()), filter, orderby, countOnly == "true" || countOnly == "")
    }
}