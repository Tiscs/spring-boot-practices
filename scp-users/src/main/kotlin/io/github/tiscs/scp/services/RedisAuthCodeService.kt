package io.github.tiscs.scp.services

import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStringCommands
import org.springframework.data.redis.core.types.Expiration
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy
import org.springframework.stereotype.Component

const val KEY_PREFIX = "OAUTH2_AUTHORIZATION_CODES_"

@Component
class RedisAuthCodeService(
        private val redisConnectionFactory: RedisConnectionFactory
) : RandomValueAuthorizationCodeServices() {
    private val serializationStrategy = JdkSerializationStrategy()

    private fun getConnection(): RedisConnection = redisConnectionFactory.connection

    override fun remove(code: String): OAuth2Authentication? {
        val key = serializationStrategy.serialize(KEY_PREFIX + code)
        val data: ByteArray?
        val connection = getConnection()
        try {
            connection.multi()
            connection.get(key)
            connection.del(key)
            val results = connection.exec()
            data = results.firstOrNull() as ByteArray?
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
        val key = serializationStrategy.serialize(KEY_PREFIX + code)
        val data = serializationStrategy.serialize(authentication)
        val connection = getConnection()
        try {
            connection.set(key, data, Expiration.seconds(5 * 60), RedisStringCommands.SetOption.UPSERT)
        } finally {
            connection.close()
        }
    }
}
