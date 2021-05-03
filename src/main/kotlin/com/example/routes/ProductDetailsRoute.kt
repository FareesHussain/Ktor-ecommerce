package com.example.routes

import com.example.database.query.getAllProducts
import com.example.database.query.getProductForID
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

fun Route.productDetailsRoute() {
    route("/product") {
        get() {
            val id = call.parameters["id"] ?: return@get call.respond(
                OK,
                KMobileResponse(false, "id parameter not found")
            )
            val productDetails = async { getProductForID(id.toInt()) }
            productDetails.await()?.let {
                call.respond(OK, getAnyRes(true, it))
                return@get
            }
            call.respond(OK, KMobileResponse(false, "Unknown error occured"))
        }
    }
}
