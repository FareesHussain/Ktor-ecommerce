package com.example.routes

import com.example.database.query.checkIfCustomerExists
import com.example.database.query.updateCustomer
import com.example.models.Customers
import com.example.responses.KMobileResponse
import com.example.responses.getResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateCustomerRoute() {
    route("/update_customer") {
        post {
            val request = try {
                call.receive<Customers>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, getResponse(false, "Invalid request json $e"))
                return@post
            }
            if(checkIfCustomerExists(request.email) == 0) {
                val updateValue = updateCustomer(request)
                call.respond(
                    HttpStatusCode.OK, KMobileResponse(
                        successful = true,
                        message = "User updated $updateValue"
                    )
                )
            } else {
                    call.respond(
                        HttpStatusCode.OK, KMobileResponse(
                        successful = false,
                        message = "User Already Exists"
                    )
                )
            }
        }
    }
}