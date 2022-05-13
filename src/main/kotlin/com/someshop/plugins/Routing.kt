package com.someshop.plugins

import com.someshop.api.authApiRouting
import com.someshop.api.spotApiRouting
import com.someshop.api.transactionApiRouting
import com.someshop.api.userApiRouting
import com.someshop.auth.AuthConfig
import com.someshop.auth.AuthData
import com.someshop.database.DatabaseManager
import com.someshop.repository.DatabaseRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(authConfig: AuthConfig, dbManager: DatabaseManager) {

    val repo = DatabaseRepository(dbManager)

    routing {

        get("/openapi.json") {
//            call.respond(application.openAPIGen.api.serialize())
        }

        get("/swagger-ui") {
//            call.respondRedirect("/swagger-ui/index.html?url=$OPEN_API_JSON_PATH", true)
        }

        get("/test") {
            call.respond(AuthData("sus", "amogus"))
        }

        route("/auth") {
            authApiRouting(repo, authConfig)
        }

        route("/api") {
            spotApiRouting(repo)
            transactionApiRouting(repo)
            userApiRouting(repo, authConfig)
        }
    }
}
