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

fun Route.userApiRouting(repository: DatabaseRepository, authConfig: AuthConfig) {

    route("/user") {

        get("/all") {
            val users = repository.getAllUsers()
            call.respond(
                status = HttpStatusCode.OK,
                message = users
            )
        }

        get("/byName/{name}") {
            val name = call.parameters["name"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Name parameter required"
            )

            val user = repository.getUserByName(name) ?: return@get call.respond(
                status = HttpStatusCode.NotFound,
                message = "User not found"
            )
            call.respond(
                status = HttpStatusCode.OK,
                message = user
            )
        }

        get("/byId/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Id parameter required"
            )
            val intId = id.toIntOrNull() ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Id must be integer"
            )

            val user = repository.getUserById(intId) ?: return@get call.respond(
                status = HttpStatusCode.NotFound,
                message = "User not found"
            )
            call.respond(
                status = HttpStatusCode.OK,
                message = user
            )
        }

        authenticate("jwtAuth") {

            route("/me") {

                get {
                    val authData = getUserFromPrincipal(repository::getUserAuthData) ?: return@get

                    call.respond(
                        status = HttpStatusCode.OK,
                        message = authData
                    )
                }

                put {
                    val newData = call.receive<AuthData>()
                    if (newData.name.isBlank() || newData.password.isBlank()) return@put call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = "Login or password is blank"
                    )

                    val authData = getUserFromPrincipal(repository::getUserByName) ?: return@put

                    val draft = UserDraft(newData.name, newData.password)
                    if (!repository.updateUser(authData.id, draft)) return@put call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = "Auth data not updated"
                    )
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = AuthSuccess(authConfig.createToken(draft.name, draft.password))
                    )
                }
            }
        }
    }
}