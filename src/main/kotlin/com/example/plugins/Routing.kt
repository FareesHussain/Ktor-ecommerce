package com.example.plugins

import com.example.database.query.checkIfCustomerExists
import com.example.database.query.registerCustomer
import com.example.models.Customers
import com.example.requests.RegisterRequest
import com.example.responses.KMobileResponse
import com.example.responses.getResponse
import com.example.routes.*
import com.google.gson.Gson
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

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
        addProductsToCartRoute()
        placeOrderRoute()
        getOrdersRoute()
        getCustomerDetailsRoute()
        post("/test") {
            call.respond(HttpStatusCode.OK, getResponse(true, "Working perfectly"))
        }
    }
}

