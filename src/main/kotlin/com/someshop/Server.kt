package com.someshop

import com.someshop.auth.AuthConfig
import com.someshop.database.DatabaseManager
import com.someshop.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    val appConfig = HoconApplicationConfig(ConfigFactory.load())
    val authConfig = AuthConfig(appConfig)
    val dbManager = DatabaseManager(appConfig)

    configureLogging()
    configureSerialization()
    configureAuth(authConfig)
    configureRouting(authConfig, dbManager)
}
