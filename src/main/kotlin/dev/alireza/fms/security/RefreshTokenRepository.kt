package dev.alireza.fms.security

import java.time.LocalDateTime

interface RefreshTokenRepository {

    fun hasAnyUnexpiredToken(userId: Long, token: String): Boolean

    fun insertNewRefreshToken(userId: Long, expiredAt: LocalDateTime): String
}