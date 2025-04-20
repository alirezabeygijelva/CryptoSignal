package dev.alireza.fms.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class BearerAuthenticationToken(
    private val principal: Any,
    private val credentials: Any,
    authorities: MutableCollection<out GrantedAuthority>?
) : AbstractAuthenticationToken(authorities) {

    override fun getCredentials(): Any {
        return this.credentials
    }

    override fun getPrincipal(): Any {
        return this.principal
    }
}