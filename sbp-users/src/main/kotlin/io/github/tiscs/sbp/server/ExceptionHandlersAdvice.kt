package io.github.tiscs.sbp.server

import io.github.tiscs.sbp.models.setProperty
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

@RestControllerAdvice
class ExceptionHandlersAdvice {
    @ExceptionHandler(value = [WebServiceException::class])
    @ResponseBody
    fun handleWebServiceException(ex: WebServiceException): ProblemDetail {
        val status = if (ex is HttpServiceException) {
            ex.status
        } else {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ProblemDetail.forStatus(status).also {
            it.type = URI.create(ex.type)
            it.title = ex.error
            it.detail = ex.message
            it.setProperty("cause", ex.cause)
            it.setProperty("description", ex.description)
        }
    }
}
