package com.example.routes

import com.example.database.query.updateProductsToCart
import com.example.requests.ProductAndQuantityList
import com.example.responses.getResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.addProductsToCartRoute() {
    route("/update_products_to_cart") {
        post {
            val request = try {
                call.receive<ProductAndQuantityList>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, getResponse(false, "Invalid request json $e"))
                return@post
            }
            updateProductsToCart(request).run {
                if(this > 0)
                    call.respond(HttpStatusCode.OK, getResponse(true, "Products added successfully total ammount is $this"))
                else
                    call.respond(HttpStatusCode.OK, getResponse(false, "Unknown error occured"))
            }
        }
    }
}