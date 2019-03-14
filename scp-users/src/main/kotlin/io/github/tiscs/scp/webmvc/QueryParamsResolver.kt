package io.github.tiscs.scp.webmvc

import io.github.tiscs.scp.models.Paging
import io.github.tiscs.scp.models.Query
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class QueryParamsResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == Query::class.java

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        return Query(Paging(0, 10))
    }
}