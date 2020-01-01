package io.github.tiscs.scp.controllers

import io.github.tiscs.scp.models.*
import io.github.tiscs.scp.models.Query
import io.github.tiscs.scp.snowflake.IdWorker
import io.github.tiscs.scp.webmvc.HttpServiceException
import io.swagger.annotations.Api
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.jetbrains.exposed.sql.*
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.support.MessageBuilder
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@Api(tags = ["Users"])
@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
@EnableBinding(value = [Source::class])
@Transactional
class UserController(
        private val idWorker: IdWorker,
        private val eventSource: Source
) : CurdController<User, String> {
    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class)
    )
    @RequestMapping(method = [RequestMethod.GET])
    override fun fetch(query: Query): ResponseEntity<Page<User>> {
        return ResponseEntity.ok(Page(Users.selectAll(), 0, 10, ResultRow::toUser))
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class),
            ApiResponse(code = 404, message = "Not Found", response = APIError::class)
    )
    @RequestMapping(method = [RequestMethod.GET], path = ["/{id}"])
    override fun fetch(
            @ApiParam(value = "id", required = true)
            @PathVariable id: String): ResponseEntity<User> {
        val result = Users.select { Users.id eq id }.singleOrNull()?.toUser()
                ?: throw HttpServiceException(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(result)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class),
            ApiResponse(code = 404, message = "Not Found", response = APIError::class)
    )
    @RequestMapping(method = [RequestMethod.DELETE], path = ["/{id}"])
    override fun delete(
            @ApiParam(value = "id", required = true)
            @PathVariable id: String): ResponseEntity<Void> {
        val count = Users.deleteWhere { Users.id eq id }
        return if (count > 0) {
            ResponseEntity.ok().build()
        } else {
            throw HttpServiceException(HttpStatus.NOT_FOUND)
        }
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class)
    )
    @RequestMapping(method = [RequestMethod.POST])
    override fun create(@RequestBody model: User): ResponseEntity<User> {
        val result = Users.insert {
            it[id] = idWorker.nextHex()
            it[username] = model.username!!
            it[name] = model.name
            it[avatar] = model.avatar
            it[gender] = model.gender
            it[birthdate] = model.birthdate
        }.resultedValues?.singleOrNull()?.toUser() ?: throw HttpServiceException(HttpStatus.INTERNAL_SERVER_ERROR)
        eventSource.output().send(MessageBuilder.withPayload(Event("USER_CREATED", result)).build())
        return ResponseEntity.ok(result)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class),
            ApiResponse(code = 404, message = "Not Found", response = APIError::class)
    )
    @RequestMapping(method = [RequestMethod.PUT])
    override fun update(@RequestBody model: User): ResponseEntity<User> {
        val count = Users.update({ Users.id eq model.id!! }) {
            it[name] = model.name
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
