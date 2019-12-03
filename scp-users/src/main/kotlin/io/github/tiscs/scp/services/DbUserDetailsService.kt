package io.github.tiscs.scp.services

import io.github.tiscs.scp.models.Users
import org.jetbrains.exposed.sql.select
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDateTime

@Component
class DbUserDetailsService : UserDetailsService {
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val row = Users.select { Users.username eq username }.singleOrNull()
                ?: throw UsernameNotFoundException("Unknown user: $username")
        return User(
                row[Users.username],
                row[Users.password] ?: "",
                !row[Users.disabled] && row[Users.accepted],
                row[Users.expiresAt]?.isBefore(LocalDateTime.now(Clock.systemUTC())) ?: true,
                true,
                true,
                emptyList()
        )
    }
}
