@file:Suppress("unused")

package io.github.tiscs.sbp.models

import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.Pattern

const val DEFAULT_ID_PATTERN = "[a-fA-F\\d]{16}"

data class Page<T>(val total: Long, val page: Int, val size: Int, val items: List<T>)

data class Event<T>(
    val type: String,
    val body: T,
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER,
    UNKNOWN,
}

data class User(
    @Pattern(regexp = DEFAULT_ID_PATTERN)
    val id: String? = null,
    @Pattern(regexp = DEFAULT_ID_PATTERN)
    val realmId: String? = null,
    val createdAt: LocalDateTime? = null,
    val expiresAt: LocalDateTime? = null,
    val disabled: Boolean? = null,
    val accepted: Boolean? = null,
    val username: String? = null,
    val nickname: String? = null,
    val avatar: String? = null,
    val gender: Gender? = null,
    val birthdate: LocalDate? = null,
)

data class Client(
    @Pattern(regexp = DEFAULT_ID_PATTERN)
    val id: String? = null,
    @Pattern(regexp = DEFAULT_ID_PATTERN)
    val vendorId: String? = null,
    val createdAt: LocalDateTime? = null,
    val expiresAt: LocalDateTime? = null,
    val disabled: Boolean? = null,
    val accepted: Boolean? = null,
    val password: String? = null,
    val name: String? = null,
    val description: String? = null,
    val scope: Set<String>? = null,
    val grantTypes: Set<String>? = null,
    val resourceIds: Set<String>? = null,
    val redirectUris: Set<String>? = null,
)
