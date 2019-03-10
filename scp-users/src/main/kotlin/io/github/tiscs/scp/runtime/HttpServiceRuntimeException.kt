package io.github.tiscs.scp.runtime

import org.springframework.http.HttpStatus

class HttpServiceRuntimeException(
        val httpStatus: HttpStatus,
        val error: String = httpStatus.reasonPhrase,
        val description: Any? = null,
        message: String? = null,
        cause: Throwable? = null) : RuntimeException(message, cause) {
}