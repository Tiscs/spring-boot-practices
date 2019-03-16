package io.github.tiscs.scp.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SizedIterable
import org.joda.time.DateTime
import org.joda.time.LocalDate
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
            this(rows.count(), page, size, if (countOnly) emptyList() else rows.limit(size, page * size).map(mapper))
}

enum class Gender {
    MALE,
    FEMALE,
    UNKNOWN
}

data class User(
        val id: UUID? = null,
        val createdAt: DateTime? = null,
        val expiresAt: DateTime? = null,
        val disabled: Boolean? = null,
        val username: String? = null,
        var name: String? = null,
        var avatar: String? = null,
        var gender: Gender? = null,
        var birthdate: LocalDate? = null
)

fun ResultRow.toUser() = User(
        id = this.tryGet(Users.id),
        createdAt = this.tryGet(Users.createdAt),
        expiresAt = this.tryGet(Users.expiresAt),
        disabled = this.tryGet(Users.disabled),
        username = this.tryGet(Users.username),
        name = this.tryGet(Users.name),
        avatar = this.tryGet(Users.avatar),
        gender = this.tryGet(Users.gender),
        birthdate = this.tryGet(Users.birthdate)?.toLocalDate()
)
