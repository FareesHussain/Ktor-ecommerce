package com.example.routes

import com.example.database.query.loginUser
import com.example.requests.LoginRequest
import com.example.responses.KMobileResponseWithData
import com.example.responses.getResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.loginCustomerRoute() {
    route("/login") {
        post {
            val request = try {
                call.receive<LoginRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, KMobileResponseWithData(false, "Invalid request json $e", "0"))
                return@post
            }
            loginUser(request).run {
                if (this>0) {
                    call.respond(HttpStatusCode.OK, KMobileResponseWithData(true, "User logged in successfully", this.toString()))
                    return@post
                } else {
                    call.respond(HttpStatusCode.OK, KMobileResponseWithData(false, "email and password doesn't match", this.toString()))
                }
            }
        }
    }
}