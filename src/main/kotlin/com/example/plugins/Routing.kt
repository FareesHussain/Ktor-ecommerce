package com.example.plugins

import com.example.responses.getResponse
import com.example.routes.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        registerCustomerRoute()
        updateCustomerRoute()
        loginCustomerRoute()
        addProductRoute()
        getAllProductsRoute()
        productDetailsRoute()
        shoppingCartRoute()
        updateProductsToCartRoute()
        placeOrderRoute()
        getOrdersRoute()
        getCustomerDetailsRoute()
        addSingleProductToCart()
        removeSingleProductFromCart()
        post("/test") {
            call.respond(HttpStatusCode.OK, getResponse(true, "Working perfectly"))
        }
    }
}

