package io.github.tiscs.scp.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Roles : Table("roles") {
    val id = varchar("id", 16)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val disabled = bool("disabled").clientDefault { false }
    val readonly = bool("readonly").clientDefault { false }
    val name = varchar("name", 32).uniqueIndex()
    val description = varchar("description", 128).nullable()

    override val primaryKey = PrimaryKey(id)
}

object Users : Table("users") {
    val id = varchar("id", 16)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val accepted = bool("accepted").clientDefault { false }
    val username = varchar("username", 32).uniqueIndex()
    val password = varchar("password", 128).nullable()
    val displayName = varchar("display_name", 32).index().nullable()
    val avatar = varchar("avatar", 128).nullable()
    val gender = enumerationByName("gender", 32, Gender::class).nullable()
    val birthdate = date("birthdate").nullable()

    override val primaryKey = PrimaryKey(id)
}

object Vendors : Table("vendors") {
    val id = varchar("id", 16)
    val ownerId = varchar("owner_id", 16).references(Users.id)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    val expiresAt = datetime("expires_at").nullable()
    val disabled = bool("disabled").clientDefault { false }
    val accepted = bool("accepted").clientDefault { false }
    val name = varchar("name", 32)
    val description = varchar("description", 128).nullable()

    override val primaryKey = PrimaryKey(id)
}

object Clients : Table("clients") {
    val id = varchar("id", 16)
    val vendorId = varchar("vendor_id", 16).references(Vendors.id)
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
