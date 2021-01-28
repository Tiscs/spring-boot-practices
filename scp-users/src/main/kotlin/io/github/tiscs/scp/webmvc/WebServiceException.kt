package io.github.tiscs.scp.webmvc

import org.springframework.http.HttpStatus

open class WebServiceException(
    val error: String,
    val description: Any? = null,
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

class HttpServiceException(
    val status: HttpStatus,
    error: String = status.reasonPhrase,
    description: Any? = null,
    message: String? = null,
    cause: Throwable? = null,
) : WebServiceException(error, description, message, cause) {
    constructor(status: HttpStatus, wse: WebServiceException) : this(
        status, wse.error, wse.description, wse.message, wse.cause,
    )
}
