package com.example.plugins

import com.example.routing.userRouting
import com.example.services.JwtService
import com.example.services.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userRepository: UserRepository,
    jwtService: JwtService
) {
    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, message = "Hello World!")
        }

        route("/user"){
            userRouting(userRepository, jwtService)
        }
    }
}
