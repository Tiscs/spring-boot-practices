package io.github.tiscs.scp.controllers

import io.github.tiscs.scp.models.Page
import io.github.tiscs.scp.models.Query
import org.springframework.http.ResponseEntity

interface CurdController<M, K> {
    fun fetch(query: Query): ResponseEntity<Page<M>>
    fun fetch(id: K): ResponseEntity<M>
    fun delete(id: K): ResponseEntity<Void>
    fun create(model: M): ResponseEntity<M>
    fun update(model: M): ResponseEntity<M>
}
