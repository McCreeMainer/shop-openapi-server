package com.someshop.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*

class AuthConfig(appConfig: ApplicationConfig) {

    private val algorithm = Algorithm.HMAC512(appConfig.property("jwt.secret").getString())

    val verifier: JWTVerifier = JWT.require(algorithm)
        .build()

    fun createToken(login: String, password: String): String = JWT.create()
        .withClaim("login", login)
        .withClaim("pwdHash", password)
        .sign(algorithm)
}