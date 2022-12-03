package io.github.tiscs.sbp.sequence

import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.ReturnType
import org.springframework.data.redis.serializer.RedisSerializer

class RedisSequenceProvider(
    sequenceKey: String,
    private val connectionFactory: RedisConnectionFactory,
    minValue: Long = 0,
    maxValue: Long = Long.MAX_VALUE,
) : SequenceProvider {
    companion object {
        private val redisScript = RedisSerializer.string().serialize(
            """
            local r = redis.call('incr', KEYS[1])
            if (r < ARGV[1] + 1 or r > ARGV[2] + 1) then
              redis.call('set', KEYS[1], ARGV[1] + 1)
            end
            return redis.call('get', KEYS[1]) - 1
        """.trimIndent()
        )!!
    }

    private val minValueParam = RedisSerializer.string().serialize(minValue.toString(10))!!
    private val maxValueParam = RedisSerializer.string().serialize(maxValue.toString(10))!!
    private val sequenceKeyParam = RedisSerializer.string().serialize(sequenceKey)!!

    override fun nextValue(): Long = connectionFactory.connection.use {
        it.scriptingCommands().eval<Long>(
            redisScript, ReturnType.INTEGER, 1,
            sequenceKeyParam, minValueParam, maxValueParam,
        )!!
    }
}
