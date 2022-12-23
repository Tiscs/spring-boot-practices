package io.github.tiscs.sbp.models

import org.springframework.http.ProblemDetail

object ProblemTypes {
    const val UNKNOWN_ERROR = "urn:sbp-projects:problem-type:unknown_error"
    const val USER_NOT_FOUND = "urn:sbp-projects:problem-type:users:not_found"
    const val INVALID_FILTER_NAME = "urn:sbp-projects:problem-type:query:invalid_filter_name"
    const val INVALID_FILTER_PARAMS = "urn:sbp-projects:problem-type:query:invalid_filter_params"
}

fun ProblemDetail.setProperty(name: String, value: Any?) {
    if (value != null) {
        this.setProperty(name, value)
    }
}
