package io.github.tiscs.sbp.controllers

import io.github.tiscs.sbp.clients.GitHubClient
import io.github.tiscs.sbp.security.SecretGenerator
import io.github.tiscs.sbp.snowflake.IdWorker
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.tags.Tag
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Clock
import java.time.LocalDateTime

@Tag(name = "Utils")
@RestController
@RequestMapping(method = [RequestMethod.GET], path = ["/utils"])
class UtilsController(
    private val idWorker: IdWorker,
    private val passwordEncoder: PasswordEncoder,
    private val gitHubClient: GitHubClient,
) {
    @RequestMapping(path = ["/snowflake/long"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun nextLong(): ResponseEntity<String> {
        return ResponseEntity.ok(idWorker.nextLong().toString())
    }

    @RequestMapping(path = ["/snowflake/hex"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun nextHex(): ResponseEntity<String> {
        return ResponseEntity.ok(idWorker.nextHex())
    }

    @RequestMapping(path = ["/datetime/db"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun datetimeDBNow(): ResponseEntity<String> {
        @Suppress("SqlNoDataSourceInspection", "SqlDialectInspection")
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

    @RequestMapping(path = ["/password/random"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun randomPassword(
        @RequestParam size: Int,
        @Parameter(examples = [
            ExampleObject(name = "ALPHANUM", value = SecretGenerator.ALPHANUM, description = SecretGenerator.ALPHANUM),
            ExampleObject(name = "BASE58", value = SecretGenerator.BASE58, description = SecretGenerator.BASE58),
            ExampleObject(name = "UPPER", value = SecretGenerator.UPPER, description = SecretGenerator.UPPER),
            ExampleObject(name = "LOWER", value = SecretGenerator.LOWER, description = SecretGenerator.LOWER),
            ExampleObject(name = "DIGITS", value = SecretGenerator.DIGITS, description = SecretGenerator.DIGITS),
        ])
        @RequestParam symbols: String,
    ): ResponseEntity<String> {
        return ResponseEntity.ok(SecretGenerator.randomCode(size, symbols))
    }

    @RequestMapping(path = ["/password/encode"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun encodePassword(@RequestParam password: String): ResponseEntity<String> {
        return ResponseEntity.ok(passwordEncoder.encode(password))
    }

    @RequestMapping(path = ["/github/zen"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun fetchGitHubZen(): ResponseEntity<String> {
        return ResponseEntity.ok(gitHubClient.fetchZen())
    }
}
