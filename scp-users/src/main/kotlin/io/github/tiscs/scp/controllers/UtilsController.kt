package io.github.tiscs.scp.controllers

import io.github.tiscs.scp.snowflake.IdWorker
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["Utils"])
@RestController
@RequestMapping(method = [RequestMethod.POST], path = ["/utils"], consumes = ["application/x-www-form-urlencoded"])
class UtilsController(
        private val idWorker: IdWorker,
        private val passwordEncoder: PasswordEncoder
) {
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

    @RequestMapping(path = ["/password/encode"])
    fun encodePassword(@RequestParam password: String): ResponseEntity<String> {
        return ResponseEntity.ok(passwordEncoder.encode(password))
    }
}