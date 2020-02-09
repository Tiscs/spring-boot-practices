package io.github.tiscs.scp.services

import io.github.tiscs.scp.models.RoleUsers
import io.github.tiscs.scp.models.Roles
import io.github.tiscs.scp.models.Users
import io.github.tiscs.scp.security.User
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.select
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class DbUserDetailsService : UserDetailsService {
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val row = Users.select { Users.username.lowerCase() eq username.toLowerCase() }.singleOrNull()
                ?: throw UsernameNotFoundException("Unknown user: $username")
        val roles = (Roles innerJoin RoleUsers).slice(Roles.name).select { RoleUsers.userId eq row[Users.id] }.map {
            GrantedAuthority { "ROLE_${it[Roles.name]}" }
        }.toSet()
        return User(
                identity = row[Users.id],
                displayName = row[Users.displayName],
                avatar = row[Users.avatar],
                gender = row[Users.gender],
                birthdate = row[Users.birthdate],
                username = row[Users.username],
                password = row[Users.password] ?: "",
                enabled = !row[Users.disabled] && row[Users.accepted],
                accountNonExpired = row[Users.expiresAt]?.isBefore(LocalDateTime.now()) ?: true,
                authorities = roles
        )
    }
}
