package com.example.routes

import com.example.database.query.addProduct
import com.example.database.query.loginUser
import com.example.requests.AddProductRequest
import com.example.requests.LoginRequest
import com.example.responses.getResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.addProductRoute() {
    route("/admin_add_product") {
        post {
            val request = try {
                call.receive<AddProductRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, getResponse(false, "Invalid request json $e"))
                return@post
            }
            addProduct(request).run {
                if(this != "")
                    call.respond(OK, getResponse(true, this))
                else
                    call.respond(OK, getResponse(false, "Unknown error occured"))
            }
        }
    }
}