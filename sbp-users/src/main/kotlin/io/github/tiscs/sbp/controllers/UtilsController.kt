package io.github.tiscs.sbp.controllers

import io.github.tiscs.sbp.snowflake.IdWorker
import io.swagger.v3.oas.annotations.tags.Tag
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Clock
import java.time.LocalDateTime

@RestController
@RequestMapping(method = [RequestMethod.POST], path = ["/utils"])
@Tag(name = "Utils")
class UtilsController(
    private val idWorker: IdWorker,
    private val passwordEncoder: PasswordEncoder,
) {
    @RequestMapping(path = ["/snowflake/long"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun nextLong(): ResponseEntity<Long> {
        return ResponseEntity.ok(idWorker.nextLong())
    }

    @RequestMapping(path = ["/snowflake/hex"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun nextHex(): ResponseEntity<String> {
        return ResponseEntity.ok(idWorker.nextHex())
    }

    @RequestMapping(path = ["/datetime/db"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun datetimeDBNow(): ResponseEntity<String> {
        val now = transaction {
            exec("SELECT NOW()") {
                it.next()
                it.getTimestamp(1)
            }
        }!!.toLocalDateTime()
        return ResponseEntity.ok(now.toString())
    }

    @RequestMapping(path = ["/datetime/utc"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun datetimeUTCNow(): ResponseEntity<String> {
        return ResponseEntity.ok(LocalDateTime.now(Clock.systemUTC()).toString())
    }

    @RequestMapping(path = ["/datetime/local"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun datetimeLocalNow(): ResponseEntity<String> {
        return ResponseEntity.ok(LocalDateTime.now().toString())
    }

    @RequestMapping(path = ["/password/encode"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun encodePassword(@RequestParam password: String): ResponseEntity<String> {
        return ResponseEntity.ok(passwordEncoder.encode(password))
    }
}
