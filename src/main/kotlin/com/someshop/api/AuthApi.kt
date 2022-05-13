package com.someshop.api

import com.someshop.auth.AuthConfig
import com.someshop.auth.AuthData
import com.someshop.auth.AuthSuccess
import com.someshop.database.UserDraft
import com.someshop.repository.DatabaseRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Route.authApiRouting(repository: DatabaseRepository, authConfig: AuthConfig) {

    post("/login") {
        val authData = call.receive<AuthData>()
        if (authData.name.isBlank() || authData.password.isBlank()) {
            return@post call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Login or password is blank"
            )
        }
        val userData = repository.getUserAuthData(authData.name) ?: return@post call.respond(
            status = HttpStatusCode.Forbidden,
            message = "No such user with this name"
        )
        if (userData.password != authData.password) return@post call.respond(
            status = HttpStatusCode.Forbidden,
            message = "Incorrect password for this user"
        )
        call.respond(
            status = HttpStatusCode.OK,
            message = AuthSuccess(authConfig.createToken(userData.name, userData.password))
        )
    }

    post("/register") {
        val authData = call.receive<AuthData>()
        if (authData.name.isBlank() || authData.password.isBlank()) return@post call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Login or password is blank"
        )
        if (repository.getUserByName(authData.name) != null) return@post call.respond(
            status = HttpStatusCode.Conflict,
            message = "This name is already taken"
        )

        repository.addUser(UserDraft(authData.name, authData.password)) ?: return@post call.respond(
            status = HttpStatusCode.InternalServerError,
            message = "User is not registered"
        )
        call.respond(
            status = HttpStatusCode.Created,
            message = AuthSuccess(authConfig.createToken(authData.name, authData.password))
        )
    }
}

suspend fun <T> PipelineContext<*, ApplicationCall>.getUserFromPrincipal(getter: (String) -> T?): T? {
    val auth = call.principal<AuthData>()
    if (auth == null) {
        call.respond(
            status = HttpStatusCode.Unauthorized,
            message = "Principal required"
        )
        return null
    }

    val user = getter(auth.name)
    if (user == null) {
        call.respond(
            status = HttpStatusCode.Unauthorized,
            message = "User does not exists"
        )
    }
    return user
}
