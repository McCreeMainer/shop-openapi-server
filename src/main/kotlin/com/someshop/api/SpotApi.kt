package com.someshop.api

import com.someshop.database.SpotDraft
import com.someshop.repository.DatabaseRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.spotApiRouting(repository: DatabaseRepository) {

    route("/spot") {

        get("/all") {
            val spots = repository.getAllSpots()
            call.respond(
                status = HttpStatusCode.OK,
                message = spots
            )
        }

        get("/byName/{name}") {
            val name = call.parameters["name"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Name parameter required"
            )

            val spots = repository.getSpot(name) ?: return@get call.respond(
                status = HttpStatusCode.NotFound,
                message = "Spot not found"
            )
            call.respond(
                status = HttpStatusCode.OK,
                message = spots
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

            val spot = repository.getSpot(intId) ?: return@get call.respond(
                status = HttpStatusCode.NotFound,
                message = "Spot not found"
            )
            call.respond(
                status = HttpStatusCode.OK,
                message = spot
            )
        }

        authenticate("jwtAuth") {

            get("/my") {
                val user = getUserFromPrincipal(repository::getUserByName) ?: return@get

                val spots = repository.getSpotsByUser(user)
                call.respond(
                    status = HttpStatusCode.OK,
                    message = spots
                )
            }

            post("/add") {
                val draft = call.receive<SpotDraft>()
                if (draft.name.isBlank()) return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "Spot name is blank"
                )
                if (draft.cost < 0) return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "Spot is less than zero"
                )
                if (draft.quantity < 0) return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "Quantity is less than zero"
                )

                val user = getUserFromPrincipal(repository::getUserByName) ?: return@post

                val spot = repository.addSpot(draft, user) ?: return@post call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = "Spot not added"
                )
                call.respond(
                    status = HttpStatusCode.OK,
                    message = spot
                )
            }
        }
    }
}


