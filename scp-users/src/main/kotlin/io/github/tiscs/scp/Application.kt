package io.github.tiscs.scp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@SpringBootApplication
@EnableRedisHttpSession
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
