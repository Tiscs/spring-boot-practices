@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.github.tiscs.sbp.tables

import io.github.tiscs.sbp.models.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

fun <T> SizedIterable<ResultRow>.toPage(
    index: Int,
    size: Int,
    mapper: (ResultRow) -> T,
    countOnly: Boolean = false,
): Page<T> =
    Page(this.count(), index, size, if (countOnly) emptyList() else this.limit(size).offset(index.toLong() * size).map(mapper))

fun <T> SizedIterable<ResultRow>.toPage(
    paging: Paging,
    mapper: (ResultRow) -> T,
    countOnly: Boolean = false,
): Page<T> = toPage(paging.page, paging.size, mapper, countOnly)

object Realms : Table("realms") {
    val id = varchar("id", 16)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val disabled = bool("disabled").clientDefault { false }
    val readonly = bool("readonly").clientDefault { false }
    val name = varchar("name", 32).uniqueIndex()
    val description = varchar("description", 128).nullable()

    override val primaryKey = PrimaryKey(id)
}

object Roles : Table("roles") {
    val id = varchar("id", 16)
    val realmId = varchar("realm_id", 16).references(Realms.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val disabled = bool("disabled").clientDefault { false }
    val readonly = bool("readonly").clientDefault { false }
    val name = varchar("name", 32)
    val description = varchar("description", 128).nullable()

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(realmId, name)
    }
}

object Users : Table("users") {
    val id = varchar("id", 16)
    val realmId = varchar("realm_id", 16).references(Realms.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val accepted = bool("accepted").clientDefault { false }
    val username = varchar("username", 32)
    val password = varchar("password", 128).nullable()
    val nickname = varchar("nickname", 32).index().nullable()
    val avatar = varchar("avatar", 128).nullable()
    val gender = enumerationByName("gender", 32, Gender::class).clientDefault { Gender.UNKNOWN }
    val birthdate = date("birthdate").nullable()

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(realmId, username)
    }
}

fun ResultRow.toUser() = User(
    id = this.getOrNull(Users.id),
    realmId = this.getOrNull(Users.realmId),
    createdAt = this.getOrNull(Users.createdAt),
    expiresAt = this.getOrNull(Users.expiresAt),
    disabled = this.getOrNull(Users.disabled),
    accepted = this.getOrNull(Users.accepted),
    username = this.getOrNull(Users.username),
    nickname = this.getOrNull(Users.nickname),
    avatar = this.getOrNull(Users.avatar),
    gender = this.getOrNull(Users.gender),
    birthdate = this.getOrNull(Users.birthdate),
)

fun ResultRow.toUserDetails(roles: Iterable<String>) = object: UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles.map { GrantedAuthority { "ROLE_$it" } }
    }

    override fun getPassword() = get(Users.password)

    override fun getUsername() = get(Users.username)

    override fun isAccountNonExpired() = get(Users.expiresAt)?.isAfter(LocalDateTime.now()) != false

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}

object Vendors : Table("vendors") {
    val id = varchar("id", 16)
    val realmId = varchar("realm_id", 16).references(Realms.id)
    val ownerId = varchar("owner_id", 16).references(Users.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val accepted = bool("accepted").clientDefault { false }
    val name = varchar("name", 32)
    val description = varchar("description", 128).nullable()

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(realmId, name)
    }
}

object Clients : Table("clients") {
    val id = varchar("id", 16)
    val vendorId = varchar("vendor_id", 16).references(Vendors.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val accepted = bool("accepted").clientDefault { false }
    val password = varchar("password", 128)
    val name = varchar("name", 32)
    val description = varchar("description", 128).nullable()
    val scope = varchar("scope", 2048)
    val grantTypes = varchar("grant_types", 128).nullable() // authorization_code,refresh_token,implicit,password,client_credentials
    val resourceIds = varchar("resource_ids", 128).nullable()
    val redirectUris = varchar("redirect_uris", 128).nullable()

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(vendorId, name)
    }
}

fun ResultRow.toClient() = Client(
    id = this.getOrNull(Clients.id),
    vendorId = this.getOrNull(Clients.vendorId),
    createdAt = this.getOrNull(Clients.createdAt),
    expiresAt = this.getOrNull(Clients.expiresAt),
    disabled = this.getOrNull(Clients.disabled),
    accepted = this.getOrNull(Clients.accepted),
    password = this.getOrNull(Clients.password),
    name = this.getOrNull(Clients.name),
    description = this.getOrNull(Clients.description),
    scope = this.getOrNull(Clients.scope)?.split(' ')?.toSet(),
    grantTypes = this.getOrNull(Clients.grantTypes)?.split(' ')?.toSet(),
    resourceIds = this.getOrNull(Clients.resourceIds)?.split(' ')?.toSet(),
    redirectUris = this.getOrNull(Clients.redirectUris)?.split(' ')?.toSet(),
)

object RoleUsers : Table("role_users") {
    val roleId = varchar("role_id", 16).references(Roles.id)
    val userId = varchar("user_id", 16).references(Users.id)

    override val primaryKey = PrimaryKey(roleId, userId)
}

object VendorUsers : Table("vendor_users") {
    val vendorId = varchar("vendor_id", 16).references(Vendors.id)
    val userId = varchar("user_id", 16).references(Users.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val unionId = varchar("union_id", 32).uniqueIndex()

    override val primaryKey = PrimaryKey(vendorId, userId)
}

object ClientUsers : Table("client_users") {
    val clientId = varchar("client_id", 16).references(Clients.id)
    val userId = varchar("user_id", 16).references(Users.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val openId = varchar("open_id", 32).uniqueIndex()
    val scope = varchar("scope", 2048)

    override val primaryKey = PrimaryKey(clientId, userId)
}
