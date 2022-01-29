package io.github.tiscs.sbp.webmvc

import io.github.tiscs.sbp.models.ProblemDetails
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandlersAdvice {
    @ExceptionHandler(value = [WebServiceException::class])
    @ResponseBody
    fun handleWebServiceException(ex: WebServiceException): ResponseEntity<ProblemDetails> {
        val status = if (ex is HttpServiceException) {
            ex.status
        } else {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_PROBLEM_JSON).body(
            ProblemDetails.builder()
                .type(ex.type)
                .title(ex.error)
                .detail(ex.message)
                .status(status.value())
                .extension("cause", ex.cause)
                .extension("description", ex.description)
                .build()
        )
    }
}
