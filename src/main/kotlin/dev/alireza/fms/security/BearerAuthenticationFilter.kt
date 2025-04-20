package dev.alireza.fms.security

import dev.alireza.fms.membership.UserContext
import dev.alireza.fms.security.jwt.JwtUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class BearerAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtUtils: JwtUtils
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token: String = extractBearerToken(request)
            val subject = jwtUtils.extractSubject(token)
            val userContext: UserContext = userDetailsService.loadUserByUsername(subject) as UserContext
            val auth = createAuthentication(userContext.principal, token, userContext.authorities, request)
            val result = authenticationManager.authenticate(auth)
            val context = SecurityContextHolder.createEmptyContext()
            context.authentication = result
            SecurityContextHolder.setContext(context)
        } catch (_: Exception) {

        } finally {
            filterChain.doFilter(request, response)
        }
    }

    // -----------------------------
    // Private Helper
    // -----------------------------

    private fun extractBearerToken(request: HttpServletRequest): String {
        val authorizationHeaderValue = request.getHeader("Authorization")
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer ")) {
            return authorizationHeaderValue.substring(7)
        }
        throw BadCredentialsException("No Bearer Token was found in the Authorization header.")
    }

    private fun createAuthentication(
        principal: Any,
        credentials: Any,
        authorities: MutableCollection<out GrantedAuthority>?,
        request: HttpServletRequest
    ): Authentication {
        val authenticationToken = BearerAuthenticationToken(principal, credentials, authorities)
        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        return authenticationToken
    }
}