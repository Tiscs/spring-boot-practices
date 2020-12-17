package io.github.tiscs.scp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@SpringBootApplication
@EnableConfigServer
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
