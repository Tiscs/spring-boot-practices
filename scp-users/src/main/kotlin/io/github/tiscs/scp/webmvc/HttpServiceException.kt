package io.github.tiscs.scp.webmvc

import io.github.tiscs.scp.models.APIError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice

class HttpServiceException(
        val status: HttpStatus,
        error: String = status.reasonPhrase,
        description: Any? = null,
        message: String? = null,
        cause: Throwable? = null) : WebServiceException(error, description, message, cause) {
    constructor(status: HttpStatus, wse: WebServiceException) : this(status, wse.error, wse.description, wse.message, wse.cause)
}

@RestControllerAdvice
class GlobalRestControllerAdvice {
    @ExceptionHandler(value = [HttpServiceException::class])
    @ResponseBody
    fun handleHttpServiceException(ex: HttpServiceException): ResponseEntity<APIError> =
            ResponseEntity.status(ex.status).body(APIError(ex.error, ex.description))

    @ExceptionHandler(value = [WebServiceException::class])
    @ResponseBody
    fun handleWebServiceException(ex: WebServiceException): ResponseEntity<APIError> =
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIError(ex.error, ex.description))
}
