package io.github.tiscs.scp.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy
import org.springframework.stereotype.Component

@Component
class RedisAuthCodeService(
        @Autowired
        private val redisConnectionFactory: RedisConnectionFactory
) : RandomValueAuthorizationCodeServices() {
    private val serializationStrategy = JdkSerializationStrategy()

    private fun getConnection(): RedisConnection = redisConnectionFactory.connection

    override fun remove(code: String): OAuth2Authentication? {
        val key = serializationStrategy.serialize(code)
        val data: ByteArray?
        val connection = getConnection()
        try {
            data = connection.get(key)
        } finally {
            connection.close()
        }
        return if (data == null) {
            null
        } else {
            serializationStrategy.deserialize(data, OAuth2Authentication::class.java)
        }
    }

    override fun store(code: String, authentication: OAuth2Authentication) {
        val key = serializationStrategy.serialize(code)
        val data = serializationStrategy.serialize(authentication)
        val connection = getConnection()
        try {
            connection.set(key, data)
        } finally {
            connection.close()
        }
    }
}
