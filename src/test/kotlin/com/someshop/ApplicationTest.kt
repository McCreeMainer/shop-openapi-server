package com.someshop

import com.someshop.auth.AuthConfig
import com.someshop.database.DatabaseManager
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.plugins.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.someshop.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            val appConfig = HoconApplicationConfig(ConfigFactory.load())
            val authConfig = AuthConfig(appConfig)
            val dbManager = DatabaseManager(appConfig)
            configureRouting(authConfig, dbManager)
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}