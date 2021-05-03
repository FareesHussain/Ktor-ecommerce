package com.example.routes

import com.example.database.query.getProductForID
import com.example.database.query.getShoppingCart
import com.example.responses.KMobileResponse
import com.example.responses.getAnyRes
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.async

fun Route.shoppingCartRoute() {
    route("/shopping_cart") {
        get() {
            val id = call.parameters["customer_id"] ?: return@get call.respond(
                HttpStatusCode.OK,
                KMobileResponse(false, "customer_id parameter not found")
            )
            val shoppingCartResponse = async { getShoppingCart(id.toInt()) }
            shoppingCartResponse.await()?.let {
                call.respond(HttpStatusCode.OK, getAnyRes(true, it))
                return@get
            }
            call.respond(HttpStatusCode.OK, KMobileResponse(false, "Unknown error occured"))
        }
    }
}
