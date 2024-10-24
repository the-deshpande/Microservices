package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest (
    val username: String,
    val password: String
)