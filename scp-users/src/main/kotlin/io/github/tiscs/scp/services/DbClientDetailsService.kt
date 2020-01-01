package io.github.tiscs.scp.services

import io.github.tiscs.scp.models.Clients
import io.github.tiscs.scp.models.Vendors
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.not
import org.jetbrains.exposed.sql.select
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.ClientRegistrationException
import org.springframework.security.oauth2.provider.client.BaseClientDetails
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

private fun ResultRow.toClientDetails(): ClientDetails {
    val client = BaseClientDetails()
    client.clientId = this[Clients.id]
    client.clientSecret = this[Clients.password]
    client.registeredRedirectUri = this[Clients.redirectUris]?.split(",")?.toSet()
    client.setResourceIds(this[Clients.resourceIds]?.split(",")?.toSet())
    client.setAuthorizedGrantTypes(this[Clients.grantTypes]?.split(",")?.toSet())
    client.setScope(this[Clients.scope].split(",").toSet())
    client.setAutoApproveScopes(client.scope)
    return client
}

@Component
class DbClientDetailsService : ClientDetailsService {
    @Transactional
    override fun loadClientByClientId(clientId: String) = (Clients innerJoin Vendors).slice(Clients.columns).select {
        (Clients.id eq clientId) and Clients.accepted and not(Clients.disabled) and Vendors.accepted and not(Vendors.disabled)
    }.singleOrNull()?.toClientDetails() ?: throw ClientRegistrationException("Unknown client: $clientId")
}
