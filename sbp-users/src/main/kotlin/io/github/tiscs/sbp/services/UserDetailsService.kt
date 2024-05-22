package io.github.tiscs.sbp.services

import io.github.tiscs.sbp.tables.RoleUsers
import io.github.tiscs.sbp.tables.Roles
import io.github.tiscs.sbp.tables.Users
import io.github.tiscs.sbp.tables.toUserDetails
import org.jetbrains.exposed.sql.selectAll
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class UserDetailsService : ReactiveUserDetailsService {
    @Transactional
    override fun findByUsername(username: String): Mono<UserDetails> {
        return Mono.justOrEmpty(Users.selectAll().where {
            Users.username eq username
        }.singleOrNull()?.toUserDetails(
            (Roles innerJoin RoleUsers innerJoin Users).select(Roles.name).where {
                Users.username eq username
            }.map { it[Roles.name] }
        ))
    }
}
