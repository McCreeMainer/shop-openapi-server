package com.someshop.plugins

import com.someshop.serialization.JsonMapper.defaultMapper
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun io.ktor.server.application.Application.configureSerialization() {
    install(ContentNegotiation) {
//        json()
        jackson {
            register(ContentType.Application.Json, JacksonConverter(defaultMapper))
        }
    }
}
