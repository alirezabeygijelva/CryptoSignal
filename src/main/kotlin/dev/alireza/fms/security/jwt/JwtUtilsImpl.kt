package dev.alireza.fms.security.jwt

import dev.alireza.fms.config.ConfigProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.function.Function

@Component
class JwtUtilsImpl(
    private val properties: ConfigProperties
) : JwtUtils {

    override fun extractSubject(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    override fun extractExpiration(token: String): Date {
        return extractClaim(token, Function { obj: Claims -> obj.expiration })
    }

    override fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    override fun extractAllClaims(token: String): Claims {
        try {
            val claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .body
            return claims
        } catch (e: Exception) {
            throw RuntimeException("Can not extract claims from token")
        }
    }

    override fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    override fun generateToken(user: UserDetails): String {
        return generateToken(HashMap(), user.username)
    }

    override fun generateToken(user: UserDetails, expirationPeriodMillis: Long): String {
        return generateToken(HashMap(), user.username, expirationPeriodMillis)
    }

    override fun generateToken(extraClaims: Map<String, Any>, username: String): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    override fun generateToken(extraClaims: Map<String, Any>, username: String, expirationPeriodMillis: Long): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationPeriodMillis))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    override fun isTokenValid(token: String, username: String): Boolean {
        val subject = extractSubject(token)
        return subject == username && !isTokenExpired(token)
    }

    // ----------------------------
    // Private Helper
    // ----------------------------

    private fun getSigningKey(): Key {
        val keyByte = Decoders.BASE64.decode(properties.security.jwtSecretKey)
        return Keys.hmacShaKeyFor(keyByte)
    }
}