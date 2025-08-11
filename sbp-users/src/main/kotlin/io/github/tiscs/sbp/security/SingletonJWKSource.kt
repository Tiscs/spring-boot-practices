package io.github.tiscs.sbp.security

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext

class SingletonJWKSource<C : SecurityContext>(
    private val jwk: JWK
) : JWKSource<C> {
    override fun get(jwkSelector: JWKSelector, context: C?): List<JWK> = listOf(jwk)
}
