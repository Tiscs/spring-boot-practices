package io.github.tiscs.scp.controllers

import io.github.tiscs.scp.models.Event
import io.github.tiscs.scp.snowflake.IdWorker
import io.swagger.annotations.Api
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source
import org.springframework.http.ResponseEntity
import org.springframework.messaging.support.MessageBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

@Api(tags = ["Utils"])
@EnableBinding(value = [Source::class])
@RestController
@RequestMapping(method = [RequestMethod.POST], path = ["/utils"])
class UtilsController(
        private val idWorker: IdWorker,
        private val eventSource: Source,
        private val passwordEncoder: PasswordEncoder
) {
    companion object {
        private val logger = LoggerFactory.getLogger(UtilsController::class.java)
    }

    @RequestMapping(path = ["/snowflake/long"])
    fun nextLong(): ResponseEntity<Long> {
        return ResponseEntity.ok(idWorker.nextLong())
    }

    @RequestMapping(path = ["/snowflake/hex"])
    fun nextHex(): ResponseEntity<String> {
        return ResponseEntity.ok(idWorker.nextHex())
    }

    @RequestMapping(path = ["/snowflake/base64"])
    fun nextBase64(): ResponseEntity<String> {
        return ResponseEntity.ok(idWorker.nextBase64())
    }

    @RequestMapping(path = ["/datetime/utc"])
    fun datetimeUTCNow(): ResponseEntity<String> {
        return ResponseEntity.ok(LocalDateTime.now(Clock.systemUTC()).toString())
    }

    @RequestMapping(path = ["/datetime/local"])
    fun datetimeLocalNow(): ResponseEntity<String> {
        logger.debug(TimeZone.getDefault().id)
        return ResponseEntity.ok(LocalDateTime.now().toString())
    }

    @RequestMapping(path = ["/password/encode"], consumes = ["application/x-www-form-urlencoded"])
    fun encodePassword(@RequestParam password: String): ResponseEntity<String> {
        return ResponseEntity.ok(passwordEncoder.encode(password))
    }

    @RequestMapping(path = ["/events/publish"], consumes = ["application/json"])
    fun publishEvent(@RequestBody event: Event<Any>): ResponseEntity<Boolean> {
        return ResponseEntity.ok(eventSource.output().send(MessageBuilder.withPayload(event).build()))
    }
}
