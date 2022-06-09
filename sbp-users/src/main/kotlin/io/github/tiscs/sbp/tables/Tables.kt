@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.github.tiscs.sbp.tables

import io.github.tiscs.sbp.models.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

fun <T> SizedIterable<ResultRow>.toPage(
    index: Int,
    size: Int,
    mapper: (ResultRow) -> T,
    countOnly: Boolean = false,
): Page<T> =
    Page(this.count(), index, size, if (countOnly) emptyList() else this.limit(size, index.toLong() * size).map(mapper))

fun <T> SizedIterable<ResultRow>.toPage(
    paging: Paging,
    mapper: (ResultRow) -> T,
    countOnly: Boolean = false,
): Page<T> = toPage(paging.page, paging.size, mapper, countOnly)

object Roles : Table("roles") {
    val id = hexLong("id")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val disabled = bool("disabled").clientDefault { false }
    val readonly = bool("readonly").clientDefault { false }
    val name = varchar("name", 32).uniqueIndex()
    val description = varchar("description", 128).nullable()

    override val primaryKey = PrimaryKey(id)
}

object Users : Table("users") {
    val id = hexLong("id")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val accepted = bool("accepted").clientDefault { false }
    val username = varchar("username", 32).uniqueIndex()
    val password = varchar("password", 128).nullable()
    val displayName = varchar("display_name", 32).index().nullable()
    val avatar = varchar("avatar", 128).nullable()
    val gender = enumerationByName("gender", 32, Gender::class).clientDefault { Gender.UNKNOWN }
    val birthdate = date("birthdate").nullable()

    override val primaryKey = PrimaryKey(id)
}

fun ResultRow.toUser() = User(
    id = this.getOrNull(Users.id),
    createdAt = this.getOrNull(Users.createdAt),
    expiresAt = this.getOrNull(Users.expiresAt),
    disabled = this.getOrNull(Users.disabled),
    accepted = this.getOrNull(Users.accepted),
    username = this.getOrNull(Users.username),
    displayName = this.getOrNull(Users.displayName),
    avatar = this.getOrNull(Users.avatar),
    gender = this.getOrNull(Users.gender),
    birthdate = this.getOrNull(Users.birthdate),
)

object Vendors : Table("vendors") {
    val id = hexLong("id")
    val ownerId = hexLong("owner_id").references(Users.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val accepted = bool("accepted").clientDefault { false }
    val name = varchar("name", 32)
    val description = varchar("description", 128).nullable()

    override val primaryKey = PrimaryKey(id)
}

object Clients : Table("clients") {
    val id = hexLong("id")
    val vendorId = hexLong("vendor_id").references(Vendors.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val accepted = bool("accepted").clientDefault { false }
    val password = varchar("password", 128)
    val name = varchar("name", 32).nullable()
    val description = varchar("description", 128).nullable()
    val scope = varchar("scope", 2048)
    val grantTypes = varchar("grant_types", 128).nullable() // authorization_code,refresh_token,implicit,password,client_credentials
    val resourceIds = varchar("resource_ids", 128).nullable()
    val redirectUris = varchar("redirect_uris", 128).nullable()

    override val primaryKey = PrimaryKey(id)
}

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
    redirectUris = this.getOrNull(Clients.redirectUris)?.split(',')?.toSet(),
)

object RoleUsers : Table("role_users") {
    val roleId = hexLong("role_id").references(Roles.id)
    val userId = hexLong("user_id").references(Users.id)

    override val primaryKey = PrimaryKey(roleId, userId)
}

object VendorUsers : Table("vendor_users") {
    val vendorId = hexLong("vendor_id").references(Vendors.id)
    val userId = hexLong("user_id").references(Users.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val unionId = varchar("union_id", 32).uniqueIndex()

    override val primaryKey = PrimaryKey(vendorId, userId)
}

object ClientUsers : Table("client_users") {
    val clientId = hexLong("client_id").references(Clients.id)
    val userId = hexLong("user_id").references(Users.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val openId = varchar("open_id", 32).uniqueIndex()
    val scope = varchar("scope", 2048)

    override val primaryKey = PrimaryKey(clientId, userId)
}
