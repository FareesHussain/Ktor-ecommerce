package com.example.routes

import com.example.database.query.addProductsToOrder
import com.example.database.query.getShoppingCart
import com.example.requests.ProductAndQuantityList
import com.example.responses.KMobileResponse
import com.example.responses.getAnyRes
import com.example.responses.getResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.async

fun Route.placeOrderRoute() {
    route("/place_order") {
        post() {
            val request = try {
                call.receive<ProductAndQuantityList>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, getResponse(false, "Invalid request json $e"))
                return@post
            }
            addProductsToOrder(request)
            call.respond(HttpStatusCode.OK, KMobileResponse(true, "Products added successfully"))
        }
    }
}
