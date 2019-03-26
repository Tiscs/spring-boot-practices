package io.github.tiscs.scp.services

import io.github.tiscs.scp.models.Clients
import org.jetbrains.exposed.sql.select
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.ClientRegistrationException
import org.springframework.security.oauth2.provider.client.BaseClientDetails
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class DbClientDetailsService : ClientDetailsService {
    @Transactional
    override fun loadClientByClientId(clientId: String): ClientDetails {
        val row = Clients.select { Clients.username eq clientId }.singleOrNull()
                ?: throw ClientRegistrationException("Unknown client: $clientId")
        val clientDetails = BaseClientDetails(
                row[Clients.username],
                row[Clients.resourceIds],
                "OPENID",
                row[Clients.grantTypes],
                null,
                row[Clients.redirectUris]
        )
        clientDetails.clientSecret = row[Clients.password]
        clientDetails.setAutoApproveScopes(clientDetails.scope)
        return clientDetails
    }
}
