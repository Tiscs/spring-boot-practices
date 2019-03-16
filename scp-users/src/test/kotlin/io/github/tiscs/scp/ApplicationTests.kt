package io.github.tiscs.scp

import io.github.tiscs.scp.models.Clients
import io.github.tiscs.scp.models.Users
import org.jetbrains.exposed.sql.SchemaUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test", "local")
class ApplicationTests {
    @Test
    fun contextLoads() {
    }

    @Test
    @Transactional
    fun exposedCreate() {
        SchemaUtils.create(Users, Clients)
    }
}