package io.github.tiscs.scp.services

import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class RedisAuthCodeService : RandomValueAuthorizationCodeServices() {
    private val authorizationCodeStore = ConcurrentHashMap<String, OAuth2Authentication>()

    override fun remove(code: String): OAuth2Authentication? {
        return authorizationCodeStore.remove(code)
    }

    override fun store(code: String, authentication: OAuth2Authentication) {
        this.authorizationCodeStore[code] = authentication
    }
}
