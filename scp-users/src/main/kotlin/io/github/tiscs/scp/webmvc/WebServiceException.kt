package io.github.tiscs.scp.webmvc

open class WebServiceException(
    val error: String,
    val description: Any? = null,
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
