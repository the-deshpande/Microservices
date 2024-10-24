package com.example.routing

import com.example.models.UserRequest
import com.example.services.JwtService
import com.example.services.UserRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

fun Route.userRouting(
    userRepository: UserRepository,
    jwtService: JwtService
){
    get("/get-all"){
        return@get call.respond(HttpStatusCode.OK, userRepository.allUsers())
    }
    post("/login"){
        val login = call.receive<UserRequest>()

        val user = userRepository.getByUsername(login.username) ?: return@post call.respond(HttpStatusCode.NotFound)
        if (user.password != login.password) return@post call.respond(HttpStatusCode.BadGateway)

        return@post call.respond(HttpStatusCode.Accepted, message = jwtService.createAccessToken(user))
    }

    post("/register"){
        val regUser = call.receive<UserRequest>()

        if(userRepository.createUser(regUser))
            return@post call.respond(HttpStatusCode.Created)
        return@post call.respond(HttpStatusCode.NotAcceptable)
    }

    authenticate {
        delete("/delete") {
            val user = userRepository.getByUsername(
                call.principal<JWTPrincipal>()
                !!.payload.getClaim("username")
                    .asString()
            ) ?: return@delete call.respond(HttpStatusCode.NotAcceptable)

            if(userRepository.deleteUser(user))
                return@delete call.respond(HttpStatusCode.OK)
            return@delete call.respond(HttpStatusCode.NotFound)
        }

        get("/secret"){
            val user = userRepository.getByUsername(
                call.principal<JWTPrincipal>()
                !!.payload.getClaim("username")
                    .asString()
            ) ?: return@get call.respond(HttpStatusCode.NotAcceptable, "Invalid")

            call.respond(HttpStatusCode.Accepted, "You found the secret ${user.username}")
        }

        get("/auth"){
            call.respond(message = call.principal<JWTPrincipal>()!!.payload.getClaim("username").asString())
        }
    }
}