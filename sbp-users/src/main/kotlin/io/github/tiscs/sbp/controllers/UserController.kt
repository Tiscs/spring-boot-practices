package io.github.tiscs.sbp.controllers

import io.github.tiscs.sbp.models.*
import io.github.tiscs.sbp.models.Query
import io.github.tiscs.sbp.openapi.ApiFilter
import io.github.tiscs.sbp.openapi.ApiFilters
import io.github.tiscs.sbp.security.SecuritySchemeKeys
import io.github.tiscs.sbp.server.HttpServiceException
import io.github.tiscs.sbp.snowflake.IdWorker
import io.github.tiscs.sbp.tables.Users
import io.github.tiscs.sbp.tables.toPage
import io.github.tiscs.sbp.tables.toUser
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@Tag(name = "Users")
@Transactional
@RestController
@RequestMapping("/users")
class UserController(
    private val idWorker: IdWorker,
    private val rabbitTemplate: RabbitTemplate,
) : CurdController<User, String> {
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ProblemDetail::class))]),
    )
    @SecurityRequirement(name = SecuritySchemeKeys.BEARER_TOKEN)
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = [RequestMethod.GET], path = ["/me"])
    fun fetch(user: Authentication): ResponseEntity<User> {
        val result = Users.selectAll().where { Users.username eq user.name }.singleOrNull()?.toUser()
            ?: throw HttpServiceException(HttpStatus.UNAUTHORIZED, ProblemTypes.USER_NOT_FOUND)
        return ResponseEntity.ok(result)
    }

    @ApiFilters(
        ApiFilter(
            "nickname_like", "'% von Ulrich'",
            "The underscore ( `_` ) wildcard matches any single character,  \n" +
                "The percentage ( `%` ) wildcard matches any string of zero or more characters.",
            [String::class]
        ),
    )
    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ProblemDetail::class))]),
    )
    @RequestMapping(method = [RequestMethod.GET])
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    override fun fetch(query: Query): ResponseEntity<Page<User>> {
        return ResponseEntity.ok(
            Users.selectAll().let { base ->
                when (query.filter?.name) {
                    "nickname_like" -> {
                        val pattern = query.filter.getParam<String>(0)
                            ?: throw HttpServiceException(HttpStatus.BAD_REQUEST, ProblemTypes.INVALID_FILTER_PARAMS)
                        base.andWhere {
                            Users.nickname like pattern
                        }
                    }
                    null -> base
                    else -> throw HttpServiceException(HttpStatus.BAD_REQUEST, ProblemTypes.INVALID_FILTER_NAME)
                }
            }.toPage(query.paging, ResultRow::toUser, query.countOnly)
        )
    }

    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ProblemDetail::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ProblemDetail::class))]),
    )
    @RequestMapping(method = [RequestMethod.GET], path = ["/{id:$DEFAULT_ID_PATTERN}"])
    override fun fetch(@PathVariable id: String): ResponseEntity<User> {
        val result = Users.selectAll().where { Users.id eq id }.singleOrNull()?.toUser()
            ?: throw HttpServiceException(HttpStatus.NOT_FOUND, ProblemTypes.USER_NOT_FOUND)
        return ResponseEntity.ok(result)
    }

    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ProblemDetail::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ProblemDetail::class))]),
    )
    @RequestMapping(method = [RequestMethod.DELETE], path = ["/{id:$DEFAULT_ID_PATTERN}"])
    override fun delete(@PathVariable id: String): ResponseEntity<Void> {
        val count = Users.deleteWhere { Users.id eq id }
        return if (count > 0) {
            ResponseEntity.ok().build()
        } else {
            throw HttpServiceException(HttpStatus.NOT_FOUND, ProblemTypes.USER_NOT_FOUND)
        }
    }

    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ProblemDetail::class))]),
    )
    @RequestMapping(method = [RequestMethod.POST])
    override fun create(@RequestBody model: User): ResponseEntity<User> {
        return ResponseEntity.ok(
            Users.insert {
                it[id] = idWorker.nextHex()
                it[realmId] = model.realmId!!
                it[username] = model.username!!
                it[nickname] = model.nickname
                it[avatar] = model.avatar
                it[gender] = model.gender ?: Gender.UNKNOWN
                it[birthdate] = model.birthdate
            }.resultedValues?.singleOrNull()?.toUser()?.also {
                rabbitTemplate.convertAndSend("users.events", Event("USER_CREATED", it))
            } ?: throw HttpServiceException(HttpStatus.INTERNAL_SERVER_ERROR, ProblemTypes.UNKNOWN_ERROR)
        )
    }

    @ApiResponses(
        ApiResponse(responseCode = "200"),
        ApiResponse(responseCode = "400", content = [Content(schema = Schema(implementation = ProblemDetail::class))]),
        ApiResponse(responseCode = "404", content = [Content(schema = Schema(implementation = ProblemDetail::class))]),
    )
    @RequestMapping(method = [RequestMethod.PUT])
    override fun update(@RequestBody model: User): ResponseEntity<User> {
        val count = Users.update({ Users.id eq model.id!! }) {
            it[nickname] = model.nickname
            it[avatar] = model.avatar
            it[gender] = model.gender ?: Gender.UNKNOWN
            it[birthdate] = model.birthdate
        }
        return if (count > 0) {
            ResponseEntity.ok(model)
        } else {
            throw HttpServiceException(HttpStatus.NOT_FOUND, ProblemTypes.USER_NOT_FOUND)
        }
    }
}
