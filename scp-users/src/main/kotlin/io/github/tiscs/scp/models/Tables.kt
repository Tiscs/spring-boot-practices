package io.github.tiscs.scp.models

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import java.util.*

object Users : Table("users") {
    val id = uuid("id").primaryKey().clientDefault { UUID.randomUUID() }
    val name = varchar("name", 32).nullable()
}

fun ResultRow.toUser() = User(this[Users.id], this[Users.name])
fun Iterable<ResultRow>.toUsers() = this.map { r -> r.toUser() }
