package io.github.tiscs.scp.webmvc

import io.github.tiscs.scp.models.APIError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

class HttpServiceException(
        val status: HttpStatus,
        val error: String = status.reasonPhrase,
        val description: Any? = null,
        message: String? = null,
        cause: Throwable? = null) : RuntimeException(message, cause)

@ControllerAdvice(annotations = [RestController::class])
class GlobalControllerAdvice {
    @ExceptionHandler(value = [HttpServiceException::class])
    @ResponseBody
    fun handleHttpServiceException(ex: HttpServiceException): ResponseEntity<APIError> =
            ResponseEntity.status(ex.status).body(APIError(ex.error, ex.description))
}