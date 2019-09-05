package io.github.tiscs.scp.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@Configuration
@ConditionalOnProperty(prefix = "spring.session", name = ["store-type"], havingValue = "redis")
@EnableRedisHttpSession
class RedisSessionConfig
