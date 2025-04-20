package dev.alireza.fms.security

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val tokenType: String
)