package dev.alireza.fms.security.jwt

import io.jsonwebtoken.Claims
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.function.Function

interface JwtUtils {

    fun extractSubject(token: String): String

    fun extractExpiration(token: String): Date

    fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T

    fun extractAllClaims(token: String): Claims

    fun isTokenExpired(token: String): Boolean

    fun generateToken(user: UserDetails): String

    fun generateToken(user: UserDetails, expirationPeriodMillis: Long): String

    fun generateToken(extraClaims: Map<String, Any>, username: String): String

    fun generateToken(extraClaims: Map<String, Any>, username: String, expirationPeriodMillis: Long): String

    fun isTokenValid(token: String, username: String): Boolean
}