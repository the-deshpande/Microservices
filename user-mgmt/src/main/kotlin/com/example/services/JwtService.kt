package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.Users
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*

class JwtService(
    application: Application,
    private val userRepository: UserRepository
) {
    private val jwtSecret = application.environment.config.property("jwt.secret").getString()

    val jwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(jwtSecret))
        .build()

    fun createAccessToken(user: Users): String = JWT
        .create()
        .withClaim("username", user.username)
        .sign(Algorithm.HMAC256(jwtSecret))

    fun customValidator(credential: JWTCredential): JWTPrincipal?{
        userRepository.getByUsername(credential.payload.getClaim("username").asString()) ?: return null
        return JWTPrincipal(credential.payload)
    }
}