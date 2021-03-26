package io.github.tiscs.scp.security

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenEnhancer

class TokenClaimsParser : TokenEnhancer {
    override fun enhance(accessToken: OAuth2AccessToken, authentication: OAuth2Authentication): OAuth2AccessToken {
        val principal = authentication.principal
        if (accessToken is DefaultOAuth2AccessToken && principal is User) {
            accessToken.additionalInformation = mapOf(
                "user_id" to principal.getIdentity(),
                "display_name" to principal.getDisplayName(),
                "avatar" to principal.getAvatar(),
                "gender" to principal.getGender(),
                "birthdate" to principal.getBirthdate()?.toString(),
            )
        }
        return accessToken
    }
}
