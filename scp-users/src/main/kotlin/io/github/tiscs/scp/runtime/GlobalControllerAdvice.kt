package io.github.tiscs.scp.runtime

import io.github.tiscs.scp.models.APIError
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@ControllerAdvice(annotations = [RestController::class])
class GlobalControllerAdvice {
    @ExceptionHandler(value = [HttpServiceRuntimeException::class])
    @ResponseBody
    fun HandleHttpServiceRuntimeException(ex: HttpServiceRuntimeException): ResponseEntity<APIError> {
        val error = APIError(ex.error, ex.description)
        return ResponseEntity.status(ex.httpStatus).body(error)
    }
}