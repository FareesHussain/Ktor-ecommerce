package com.example.routes

import com.example.database.query.getAllProducts
import com.example.responses.KMobileResponse
import com.example.responses.getAnyRes
import com.example.responses.getResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.async
import java.lang.Exception

fun Route.getAllProductsRoute() {
    route("/get_all_products") {
        get() {
            val brand = call.parameters["brand"]
            val allProducts = async { getAllProducts(brand) }
            allProducts.await().run {
                if (this.isNotEmpty()) {
                    call.respond(OK, getAnyRes(true, this))
                } else {
                    call.respond(OK, KMobileResponse(false, "Unknown error occured"))
                }
            }

        }
    }
}
