package io.github.tiscs.scp.controllers

import io.github.tiscs.scp.models.*
import io.github.tiscs.scp.models.Query
import io.github.tiscs.scp.openapi.ApiFilter
import io.github.tiscs.scp.openapi.ApiFilters
import io.github.tiscs.scp.snowflake.IdWorker
import io.github.tiscs.scp.webmvc.HttpServiceException
import io.swagger.v3.oas.annotations.tags.Tag
import org.jetbrains.exposed.sql.*
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.support.MessageBuilder
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
@EnableBinding(value = [Source::class])
@Tag(name = "Users")
class UserController(
        private val idWorker: IdWorker,
        private val eventSource: Source
) : CurdController<User, String> {
    @ApiFilters(
            ApiFilter("name_like", "'% von Ulrich'",
                    "The percentage ( `_` ) wildcard matches any single character, \n" +
                            "The underscore ( `%` ) wildcard matches any string of zero or more characters."
            )
    )
    @RequestMapping(method = [RequestMethod.GET])
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    override fun fetch(query: Query): ResponseEntity<Page<User>> {
        var users = Users.selectAll()
        if (query.filter?.name == "name_like") {
            val ps = query.filter.mapParams("pattern")
            val np = ps["pattern"]?.toString()
                    ?: throw HttpServiceException(HttpStatus.BAD_REQUEST, description = "Invalid filter parameters.")
            users = users.andWhere { Users.displayName like np }
        }
        return ResponseEntity.ok(users.toPage(0, 10, ResultRow::toUser, query.countOnly))
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/{id}"])
    override fun fetch(@PathVariable id: String): ResponseEntity<User> {
        val result = Users.select { Users.id eq id }.singleOrNull()?.toUser()
                ?: throw HttpServiceException(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(result)
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/{id}"])
    override fun delete(@PathVariable id: String): ResponseEntity<Void> {
        val count = Users.deleteWhere { Users.id eq id }
        return if (count > 0) {
            ResponseEntity.ok().build()
        } else {
            throw HttpServiceException(HttpStatus.NOT_FOUND)
        }
    }

    @RequestMapping(method = [RequestMethod.POST])
    override fun create(@RequestBody model: User): ResponseEntity<User> {
        val result = Users.insert {
            it[id] = idWorker.nextHex()
            it[username] = model.username!!
            it[displayName] = model.displayName
            it[avatar] = model.avatar
            it[gender] = model.gender
            it[birthdate] = model.birthdate
        }.resultedValues?.singleOrNull()?.toUser() ?: throw HttpServiceException(HttpStatus.INTERNAL_SERVER_ERROR)
        eventSource.output().send(MessageBuilder.withPayload(Event("USER_CREATED", result)).build())
        return ResponseEntity.ok(result)
    }

    @RequestMapping(method = [RequestMethod.PUT])
    override fun update(@RequestBody model: User): ResponseEntity<User> {
        val count = Users.update({ Users.id eq model.id!! }) {
            it[displayName] = model.displayName
            it[avatar] = model.avatar
            it[gender] = model.gender
            it[birthdate] = model.birthdate
        }
        return if (count > 0) {
            eventSource.output().send(MessageBuilder.withPayload(Event("USER_UPDATED", model)).build())
            ResponseEntity.ok(model)
        } else {
            throw HttpServiceException(HttpStatus.NOT_FOUND)
        }
    }
}
