package dev.alireza.fms.security

import dev.alireza.fms.membership.User
import dev.alireza.fms.membership.UserContext
import dev.alireza.fms.security.jwt.JwtUtils
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

class BearerAuthenticationProvider(
    private val jwtUtils: JwtUtils
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication {
        try {
            val token = authentication!!.credentials as String
            val user = UserContext(authentication.principal as User)
            if (jwtUtils.isTokenValid(token, user.username)) {
                val bearerToken = BearerAuthenticationToken(
                    authentication.principal,
                    authentication.credentials,
                    authentication.authorities
                )
                bearerToken.isAuthenticated = true
                return bearerToken
            } else {
                throw BadCredentialsException("Authentication failed due to invalid token.")
            }
        } catch (e: Exception) {
            throw BadCredentialsException("Authentication failed due to invalid token.")
        }
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return BearerAuthenticationToken::class.java.isAssignableFrom(authentication!!)
    }
}