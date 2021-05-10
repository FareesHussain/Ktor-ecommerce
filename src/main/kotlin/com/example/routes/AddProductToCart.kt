package com.example.routes

import com.example.database.query.addProductToCart
import com.example.requests.CartProductEdit
import com.example.responses.KMobileResponseWithData
import com.example.responses.getResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.addSingleProductToCart() {
    route("/add_product_to_cart") {
        post {
            val request = try {
                call.receive<CartProductEdit>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, KMobileResponseWithData(false, "Invalid request json $e", data = "null"))
                return@post
            }
            if(addProductToCart(request.product_id, request.customer_id, request.price)){
                call.respond(HttpStatusCode.OK, KMobileResponseWithData(true, "Added to cart", data = "null"))
            } else {
                call.respond(HttpStatusCode.OK, KMobileResponseWithData(false, "Unknown error occured", data = "null"))
            }
        }
    }
}
