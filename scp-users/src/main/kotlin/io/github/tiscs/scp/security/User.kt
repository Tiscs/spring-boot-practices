package io.github.tiscs.scp.security

import io.github.tiscs.scp.models.Gender
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate

class User(
    private val identity: String,
    private val displayName: String? = null,
    private val avatar: String? = null,
    private val gender: Gender? = null,
    private val birthdate: LocalDate? = null,
    private val username: String,
    private val password: String,
    private val enabled: Boolean = true,
    private val accountNonExpired: Boolean = true,
    private val accountNonLocked: Boolean = true,
    private val credentialsNonExpired: Boolean = true,
    private val authorities: Set<GrantedAuthority> = emptySet(),
) : UserDetails {
    fun getIdentity() = identity
    fun getDisplayName() = displayName
    fun getAvatar() = avatar
    fun getGender() = gender
    fun getBirthdate() = birthdate

    override fun getUsername() = username
    override fun getPassword() = password
    override fun isEnabled() = enabled
    override fun isAccountNonExpired() = accountNonExpired
    override fun isAccountNonLocked() = accountNonLocked
    override fun isCredentialsNonExpired() = credentialsNonExpired
    override fun getAuthorities() = authorities
}