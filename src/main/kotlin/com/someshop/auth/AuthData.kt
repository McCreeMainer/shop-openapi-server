package com.someshop.auth

import io.ktor.server.auth.*
import kotlinx.serialization.*

@Serializable
data class AuthData(val name: String, val password: String) : Principal

@Serializable
data class AuthSuccess(val jwtToken: String)
