package com.example

import com.example.plugins.*
import com.example.services.JwtService
import com.example.services.UserRepository
import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val cdbUsername = this.environment.config.property("couch.username").getString()
    val cdbPassword = this.environment.config.property(("couch.password")).getString()

    val client = HttpClient {
        install(Auth){
            basic {
                sendWithoutRequest { true }
                credentials { BasicAuthCredentials(
                    username = cdbUsername,
                    password = cdbPassword
                ) }
            }
        }
        install(ContentNegotiation){
            json()
        }
    }
    val userRepository = UserRepository(client)
    val jwtService = JwtService(this, userRepository)

    configureSerialization()
    configureSecurity(jwtService)
    configureRouting(userRepository, jwtService)
}
