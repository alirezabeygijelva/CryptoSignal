package dev.alireza.fms.membership

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {

    fun loadUserById(userId: Long): UserDetails

    fun createUser(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        encodedPassword: String
    )

    @PreAuthorize("#id == authentication.principal.id")
    fun updateUser(
        id: Long,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        telegramId: String?
    ): User

    @PreAuthorize("#user.id == authentication.principal.id")
    fun changePassword(oldPassword: String, newPassword: String, confirmNewPassword: String, user: User)
}