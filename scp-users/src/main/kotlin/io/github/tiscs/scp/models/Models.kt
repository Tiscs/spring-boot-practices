package io.github.tiscs.scp.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SizedIterable
import java.util.*

private val EmptyUUID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")

data class APIError(
        @JsonProperty("error")
        val error: String,
        @JsonProperty("error_description")
        val description: Any? = null
)

data class Page<T>(val total: Int, val page: Int, val size: Int, val items: List<T>) {
    constructor(rows: SizedIterable<ResultRow>, page: Int, size: Int, mapper: (ResultRow) -> T, countOnly: Boolean = false) :
            this(rows.count(), page, size, if (countOnly) listOf() else rows.limit(size, page * size).map(mapper))
}

data class User(val id: UUID = EmptyUUID, var name: String? = null)

fun ResultRow.toUser() = User(this[Users.id], this.tryGet(Users.name))
