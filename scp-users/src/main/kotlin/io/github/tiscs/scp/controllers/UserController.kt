package io.github.tiscs.scp.controllers

import io.github.tiscs.scp.models.*
import io.github.tiscs.scp.models.Query
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
@RestController
@RequestMapping("/users")
@Transactional
class UserController : CurdController<User, UUID> {
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
    override fun delete(
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
    override fun create(@RequestBody model: User): ResponseEntity<User> {
        val result = Users.insert {
            it[name] = model.name
        }.resultedValues!!.single().toUser()
        return ResponseEntity.ok(result)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 400, message = "Bad Request", response = APIError::class),
            ApiResponse(code = 404, message = "Not Found", response = APIError::class)
    )
    @RequestMapping(method = [RequestMethod.PUT])
    override fun update(@RequestBody model: User): ResponseEntity<User> {
        Users.update({ Users.id eq model.id!! }) {
            it[name] = model.name
        }
        return ResponseEntity.ok(model)
    }
}
