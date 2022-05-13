package com.someshop.plugins

import com.someshop.auth.AuthConfig
import com.someshop.auth.AuthData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuth(authConfig: AuthConfig) {
    install(Authentication) {
        jwt("jwtAuth") {
            verifier(authConfig.verifier)
            validate { jwtCredential ->
                val login = jwtCredential.payload.getClaim("login").asString()
                val pwdHash = jwtCredential.payload.getClaim("pwdHash").asString()
                if (login != null && pwdHash != null) AuthData(login, pwdHash)
                else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid")
            }
        }
    }
}

