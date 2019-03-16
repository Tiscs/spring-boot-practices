package io.github.tiscs.scp.controllers

import io.github.tiscs.scp.models.*
import io.github.tiscs.scp.models.Query
import io.github.tiscs.scp.swagger.ApiFilterNames
import io.swagger.annotations.Api
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.jetbrains.exposed.sql.*
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.*

@Api(tags = ["Users"])
@Transactional
@RestController
@RequestMapping("/users")
class UserController {
    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class)
    )
    @ApiFilterNames("name_like", required = true)
    @RequestMapping(method = [RequestMethod.GET])
    fun fetch(query: Query): ResponseEntity<Page<User>> {
        return ResponseEntity.ok(Page(Users.selectAll(), 0, 10, ResultRow::toUser))
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class),
            ApiResponse(code = 404, message = "Not Found", response = APIError::class)
    )
    @RequestMapping(method = [RequestMethod.GET], path = ["/{id}"])
    fun fetch(
            @ApiParam(value = "id", required = true)
            @PathVariable id: UUID): ResponseEntity<User> {
        val result = Users.select { Users.id eq id }.single().toUser()
        return ResponseEntity.ok(result)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class),
            ApiResponse(code = 404, message = "Not Found", response = APIError::class)
    )
    @RequestMapping(method = [RequestMethod.DELETE], path = ["/{id}"])
    fun delete(
            @ApiParam(value = "id", required = true)
            @PathVariable id: UUID): ResponseEntity<Void> {
        Users.deleteWhere { Users.id eq id }
        return ResponseEntity.ok().build()
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class),
            ApiResponse(code = 404, message = "Not Found", response = APIError::class)
    )
    @RequestMapping(method = [RequestMethod.POST])
    fun create(@RequestBody user: User): ResponseEntity<User> {
        val result = Users.insert {
            it[name] = user.name
        }.resultedValues!!.single().toUser()
        return ResponseEntity.ok(result)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class),
            ApiResponse(code = 404, message = "Not Found", response = APIError::class)
    )
    @RequestMapping(method = [RequestMethod.PUT])
    fun update(@RequestBody user: User): ResponseEntity<User> {
        Users.update({ Users.id eq user.id!! }) {
            it[name] = user.name
        }
        return ResponseEntity.ok(user)
    }
}