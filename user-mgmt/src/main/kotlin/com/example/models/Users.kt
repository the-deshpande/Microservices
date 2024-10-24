package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Users(
    val id: String = "",
    val rev: String = "",
    val username: String,
    val password: String
)
