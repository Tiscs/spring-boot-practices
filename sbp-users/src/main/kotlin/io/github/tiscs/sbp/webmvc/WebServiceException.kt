package io.github.tiscs.sbp.webmvc

import org.springframework.http.HttpStatus

open class WebServiceException(
    val type: String,
    val error: String,
    val description: Any? = null,
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

class HttpServiceException(
    val status: HttpStatus,
    type: String,
    error: String = status.reasonPhrase,
    description: Any? = null,
    message: String? = null,
    cause: Throwable? = null,
) : WebServiceException(type, error, description, message, cause) {
    constructor(status: HttpStatus, wse: WebServiceException) : this(
        status, wse.type, wse.error, wse.description, wse.message, wse.cause,
    )
}
