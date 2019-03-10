package io.github.tiscs.scp.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

val emptyUUID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")

class APIError(
        @JsonProperty("error")
        val error: String,
        @JsonProperty("error_description")
        val description: Any? = null
)

class Page<T>

class User(val id: UUID = emptyUUID, var name: String? = null)
