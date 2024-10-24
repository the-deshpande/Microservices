package com.example.plugins

import com.example.services.JwtService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(jwtService: JwtService) {
    authentication {
        jwt {
            verifier(jwtService.jwtVerifier)
            validate { credential -> jwtService.customValidator(credential)}
        }
    }
}
