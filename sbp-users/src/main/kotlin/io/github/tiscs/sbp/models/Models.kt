@file:Suppress("unused")

package io.github.tiscs.sbp.models

import java.time.LocalDate
import java.time.LocalDateTime

data class Page<T>(val total: Long, val page: Int, val size: Int, val items: List<T>)

enum class Gender {
    MALE,
    FEMALE,
    OTHER,
    UNKNOWN,
}

data class User(
    val id: String? = null,
    val createdAt: LocalDateTime? = null,
    val expiresAt: LocalDateTime? = null,
    val disabled: Boolean? = null,
    val accepted: Boolean? = null,
    val username: String? = null,
    val displayName: String? = null,
    val avatar: String? = null,
    val gender: Gender? = null,
    val birthdate: LocalDate? = null,
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
    val redirectUris: Set<String>? = null,
)
