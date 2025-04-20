package dev.alireza.fms.security

import dev.alireza.fms.openapi.model.SignupUserDTO
import org.springframework.security.access.prepost.PreAuthorize

interface AuthService {

    fun generateToken(username: String, password: String): AuthToken

    @PreAuthorize("#userId == authentication.principal.id")
    fun renewToken(refreshToken: String, userId: Long): AuthToken

    fun signupNewUser(signupUserDTO: SignupUserDTO)

    fun generateResetPasswordAndSendToEmail(email: String)

    fun resetPassword(resetToken: String, password: String, confirmPassword: String)
}