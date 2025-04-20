package dev.alireza.fms.membership

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserContext(val principal: User) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return principal.roles.flatMap { role ->
            listOf(role.name) + role.authorities
        }
            .map { authority -> SimpleGrantedAuthority(authority) }
            .toCollection(mutableSetOf())
    }

    override fun getPassword(): String {
        return principal.password
    }

    override fun getUsername(): String {
        return principal.clientId.toString()
    }

    override fun isEnabled(): Boolean {
        return principal.enabled
    }
}