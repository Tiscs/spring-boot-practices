@file:Suppress("unused")

package io.github.tiscs.sbp.models

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import java.time.LocalDate
import java.time.LocalDateTime

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
    @Schema(example = "04d604b6e1400000")
    @Pattern(regexp = DEFAULT_ID_PATTERN)
    val id: String? = null,
    @Schema(example = "04d604af4d400000")
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
    @Schema(example = "04d604c328800000")
    @Pattern(regexp = DEFAULT_ID_PATTERN)
    val id: String? = null,
    @Schema(example = "04d604bcd0400000")
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
