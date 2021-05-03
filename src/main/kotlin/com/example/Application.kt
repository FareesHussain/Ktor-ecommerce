package com.example

import com.example.database.connectToDatabase
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.routes.registerCustomerRoute
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*

fun main() {
    connectToDatabase()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", watchPaths = listOf("classes")) {
        configureRouting()
        configureSecurity()
        module(true)
    }.start(wait = true)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    // to negotiate the data in JSON format
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
}
