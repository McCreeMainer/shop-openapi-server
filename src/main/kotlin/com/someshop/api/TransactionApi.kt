package com.someshop.api

import com.someshop.database.TransactionDraft
import com.someshop.repository.DatabaseRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.transactionApiRouting(repository: DatabaseRepository) {

    authenticate("jwtAuth") {

        route("/transaction") {

            get("/my") {
                val user = getUserFromPrincipal(repository::getUserByName) ?: return@get
                val transactions = repository.getTransactions(user)
                call.respond(
                    status = HttpStatusCode.OK,
                    message = transactions
                )
            }

            post("/buy") {
                val draft = call.receive<TransactionDraft>()
                if (draft.quantity <= 0) return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "Quantity is less or equal to zero"
                )

                val user = getUserFromPrincipal(repository::getUserByName) ?: return@post

                val spot = repository.getSpot(draft.spotId) ?: return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "This spot does not exists"
                )

                val transactions = repository.getTransactions(spot)
                val remaining = spot.quantity - transactions.sumOf { it.quantity }
                if (draft.quantity > remaining) return@post call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "Too big quantity, only $remaining remaining for this spot"
                )

                val transaction = repository.addTransaction(draft, user) ?: return@post call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = "Spot buying error"
                )
                call.respond(
                    status = HttpStatusCode.OK,
                    message = transaction
                )
            }
        }
    }
}