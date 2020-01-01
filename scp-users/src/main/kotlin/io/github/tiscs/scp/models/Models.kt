package io.github.tiscs.scp.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SizedIterable
import java.time.LocalDate
import java.time.LocalDateTime

data class APIError(
        @JsonProperty("error")
        val error: String,
        @JsonProperty("error_description")
        val description: Any? = null
)

data class Event<T>(
        val event: String,
        val model: T
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
        val id: String? = null,
        val createdAt: LocalDateTime? = null,
        val expiresAt: LocalDateTime? = null,
        val disabled: Boolean? = null,
        val accepted: Boolean? = null,
        val username: String? = null,
        val name: String? = null,
        val avatar: String? = null,
        val gender: Gender? = null,
        val birthdate: LocalDate? = null
)

fun ResultRow.toUser() = User(
        id = this.getOrNull(Users.id),
        createdAt = this.getOrNull(Users.createdAt),
        expiresAt = this.getOrNull(Users.expiresAt),
        disabled = this.getOrNull(Users.disabled),
        accepted = this.getOrNull(Users.accepted),
        username = this.getOrNull(Users.username),
        name = this.getOrNull(Users.name),
        avatar = this.getOrNull(Users.avatar),
        gender = this.getOrNull(Users.gender),
        birthdate = this.getOrNull(Users.birthdate)
)

data class Client(
        val id: String? = null,
        val vendorId: String? = null,
        val createdAt: LocalDateTime? = null,
        val expiresAt: LocalDateTime? = null,
        val disabled: Boolean? = null,
        val accepted: Boolean? = null,
        val username: String? = null,
        val name: String? = null,
        val description: String? = null,
        val grantTypes: Set<String>? = null,
        val resourceIds: Set<String>? = null,
        val redirectUris: Set<String>? = null
)

fun ResultRow.toClient() = Client(
        id = this.getOrNull(Clients.id),
        vendorId = this.getOrNull(Clients.vendorId),
        createdAt = this.getOrNull(Clients.createdAt),
        expiresAt = this.getOrNull(Clients.expiresAt),
        disabled = this.getOrNull(Clients.disabled),
        name = this.getOrNull(Clients.name),
        description = this.getOrNull(Clients.description),
        grantTypes = this.getOrNull(Clients.grantTypes)?.split(',')?.toSet(),
        resourceIds = this.getOrNull(Clients.resourceIds)?.split(',')?.toSet(),
        redirectUris = this.getOrNull(Clients.redirectUris)?.split(',')?.toSet()
)
