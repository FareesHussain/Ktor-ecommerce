package com.example.routes

import com.example.database.query.createShoppingCartForCustomer
import com.example.database.query.checkIfCustomerExists
import com.example.database.query.registerCustomer
import com.example.models.Customers
import com.example.requests.RegisterRequest
import com.example.responses.KMobileResponse
import com.example.responses.getResponse
import io.ktor.application.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.request.ContentTransformationException
import io.ktor.response.*
import io.ktor.routing.*

fun Route.registerCustomerRoute() {
    route("/register") {
        post {
            val request = try {
                call.receive<RegisterRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest, getResponse(false, "Invalid request json $e"))
                return@post
            }
            val customer = Customers(
                customer_id = 0,
                name = request.name,
                email = request.email,
                password = request.password,
                address = request.address
            )
            if(checkIfCustomerExists(customer.email) == 0) {
                registerCustomer(customer)
                checkIfCustomerExists(customer.email).let {
                    if (it > 0){
                        createShoppingCartForCustomer(it)
                        call.respond( OK, KMobileResponse(
                            successful = true,
                            message = "user Added successfully"
                        ))
                    } else {
                        call.respond( OK, KMobileResponse(
                            successful = false,
                            message = "Unknown Error occured"
                        ))
                    }
                }
            } else {
                call.respond(OK, KMobileResponse(
                    successful = false,
                    message = "User Already Exists"
                ))
            }
        }
    }
}