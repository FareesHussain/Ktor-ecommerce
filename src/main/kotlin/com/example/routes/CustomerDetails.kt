package com.example.routes

import com.example.database.query.getAllOrders
import com.example.database.query.getCustomerDetailsForID
import com.example.database.query.loginUser
import com.example.requests.LoginRequest
import com.example.responses.KMobileResponse
import com.example.responses.KMobileResponseWithData
import com.example.responses.getAnyRes
import com.example.responses.getResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.async

fun Route.getCustomerDetailsRoute() {
    route("/customer") {
        get() {
            val id = call.parameters["customer_id"] ?: return@get call.respond(
                HttpStatusCode.OK,
                KMobileResponse(false, "customer_id parameter not found")
            )
            val customer = async { getCustomerDetailsForID(id.toInt()) }
            try {
                customer.await().let {
                    if (it.customer_id!! > 0) {
                        call.respond(HttpStatusCode.OK, KMobileResponseWithData(true, "Success", it))
                        return@get
                    } else {
                        call.respond(HttpStatusCode.OK, KMobileResponseWithData(false, "Customer Doesn't exists", "0"))
                    }
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.OK, KMobileResponseWithData(false, "Error: $e", "0"))
            }
        }
    }
}