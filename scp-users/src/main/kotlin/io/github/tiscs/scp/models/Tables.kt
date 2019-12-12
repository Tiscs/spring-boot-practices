package io.github.tiscs.scp.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.date
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.Clock
import java.time.LocalDateTime

object Users : Table("users") {
    val id = varchar("id", 16).primaryKey()
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now(Clock.systemUTC()) }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val accepted = bool("accepted").clientDefault { false }
    val username = varchar("username", 32).index(isUnique = true)
    val password = varchar("password", 128).nullable()
    val name = varchar("name", 32).index().nullable()
    val avatar = varchar("avatar", 128).nullable()
    val gender = enumerationByName("gender", 32, Gender::class).nullable()
    val birthdate = date("birthdate").nullable()
}

object Clients : Table("clients") {
    val id = varchar("id", 16).primaryKey()
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now(Clock.systemUTC()) }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val accepted = bool("accepted").clientDefault { false }
    val username = varchar("username", 32).index(isUnique = true)
    val password = varchar("password", 128)
    val name = varchar("name", 32).nullable()
    val grantTypes = varchar("grant_types", 128).nullable()
    val resourceIds = varchar("resource_ids", 128).nullable()
    val redirectUris = varchar("redirect_uris", 128).nullable()
}
