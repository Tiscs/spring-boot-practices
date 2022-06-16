package io.github.tiscs.sbp.openfeign

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.OAuth2Token

class AuthorizationRequestInterceptor : RequestInterceptor {
    override fun apply(template: RequestTemplate) {
        val token = (SecurityContextHolder.getContext().authentication?.credentials as? OAuth2Token)?.tokenValue
        if (!token.isNullOrEmpty()) {
            template.header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        }
    }
}
