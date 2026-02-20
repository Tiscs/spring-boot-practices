package io.github.tiscs.sbp.services

import io.github.tiscs.sbp.tables.RoleUsers
import io.github.tiscs.sbp.tables.Roles
import io.github.tiscs.sbp.tables.Users
import io.github.tiscs.sbp.tables.toUserDetails
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        return Users.selectAll().where {
            Users.username eq username
        }.singleOrNull()?.toUserDetails(
            (Roles innerJoin RoleUsers innerJoin Users).select(Roles.name).where {
                Users.username eq username
            }.map { it[Roles.name] }
        ) ?: throw UsernameNotFoundException("User '$username' not found")
    }
}
