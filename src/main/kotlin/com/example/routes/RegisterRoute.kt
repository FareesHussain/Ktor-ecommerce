package com.example.routes

import com.example.database.query.createShoppingCartForCustomer
import com.example.database.query.checkIfCustomerExists
import com.example.database.query.registerCustomer
import com.example.models.Customers
import com.example.requests.RegisterRequest
import com.example.responses.KMobileResponse
import com.example.responses.KMobileResponseWithData
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
                call.respond(BadRequest, KMobileResponseWithData(false, "Invalid request json $e", "0"))
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
                        call.respond( OK, KMobileResponseWithData(
                            successful = true,
                            message = "user: ${request.name} Added successfully, Please login",
                            data = it.toString()
                        ))
                    } else {
                        call.respond( OK, KMobileResponseWithData(
                            successful = false,
                            message = "Unknown Error occured",
                            data = "0"
                        ))
                    }
                }
            } else {
                call.respond(OK, KMobileResponseWithData(
                    successful = false,
                    message = "User Already Exists",
                    data = "0"
                ))
            }
        }
    }
}