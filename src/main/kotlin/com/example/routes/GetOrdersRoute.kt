package com.example.routes

import com.example.database.query.getAllOrders
import com.example.database.query.getProductForID
import com.example.responses.KMobileResponse
import com.example.responses.getAnyRes
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.async

fun Route.getOrdersRoute() {
    route("/get_orders") {
        get() {
            val id = call.parameters["customer_id"]
            val ordersList = async { getAllOrders(id?.toInt()) }
            try {
                ordersList.await().let {
                    call.respond(HttpStatusCode.OK, getAnyRes(true, it))
                    return@get
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.OK, KMobileResponse(false, "Error: $e"))
            }
        }
    }
}
