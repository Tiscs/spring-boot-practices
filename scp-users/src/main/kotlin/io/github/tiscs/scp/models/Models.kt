package io.github.tiscs.scp.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*

private val EmptyUUID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")

data class APIError(
        @JsonProperty("error")
        val error: String,
        @JsonProperty("error_description")
        val description: Any? = null
)

data class Filter(val name: String, val params: List<Any?> = listOf()) {
    fun mapParams(vararg keys: String): Map<String, Any?> =
            keys.indices.map { keys[it] to if (params.size > it) this.params[it] else null }.toMap()
}

data class Paging(val page: Int, val size: Int)

data class Query(val paging: Paging, val orderBy: String? = null, val filter: Filter? = null, val countOnly: Boolean = false)

data class Page<T>(private val total: Int, private val page: Int, private val size: Int, private val items: List<T>) {
    constructor(query: Query, page: Int, size: Int, mapper: (ResultRow) -> T, countOnly: Boolean = false) :
            this(query.count(), page, size, if (countOnly) listOf() else query.limit(size, page * size).map(mapper))
}

data class User(val id: UUID = EmptyUUID, var name: String? = null)

fun ResultRow.toUser() = User(this[Users.id], this.tryGet(Users.name))
