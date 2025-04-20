package dev.alireza.fms.dashboard.user

import dev.alireza.fms.common.dto.ResponseApi
import dev.alireza.fms.membership.User
import dev.alireza.fms.openapi.api.AuthApi
import dev.alireza.fms.openapi.model.*
import dev.alireza.fms.security.AuthService
import org.springframework.beans.BeanUtils
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset

@RestController
@RequestMapping("/api/v1")
class AuthController(
    private val authService: AuthService
) : AuthApi {

    override fun singup(signupUserDTO: SignupUserDTO): ResponseApi<AuthTokenDTO> {
        authService.signupNewUser(signupUserDTO)
        val token = authService.generateToken(signupUserDTO.email, signupUserDTO.password)
        val dto = AuthTokenDTO()
        BeanUtils.copyProperties(token, dto)
        return ResponseApi(data = dto, message = "success")
    }

    override fun getAuthToken(authTokenRequestBody: AuthTokenRequestBody): ResponseApi<AuthTokenDTO> {
        val token = authService.generateToken(authTokenRequestBody.username, authTokenRequestBody.password)
        val dto = AuthTokenDTO()
        BeanUtils.copyProperties(token, dto)
        return ResponseApi(data = dto, message = "success")
    }

    @PreAuthorize("isAuthenticated()")
    override fun getRefreshedToken(refreshTokenRequestBody: RefreshTokenRequestBody): ResponseApi<AuthTokenDTO> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val token = authService.renewToken(refreshToken = refreshTokenRequestBody.token, userId = user.id)
        val dto = AuthTokenDTO()
        BeanUtils.copyProperties(token, dto)
        return ResponseApi(data = dto, message = "success")
    }

    @PreAuthorize("isAuthenticated()")
    override fun getMe(): ResponseApi<AccountInfoDTO> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val dto = AccountInfoDTO()
        BeanUtils.copyProperties(user, dto)
        dto.roles(user.roles.map { role -> role.name })
        dto.updatedAt = user.updatedAt.atOffset(ZoneOffset.UTC)
        return ResponseApi(data = dto, message = "success")
    }

    override fun requestResetPassword(requestResetPasswordRequestBody: RequestResetPasswordRequestBody): ResponseApi<Void> {
        authService.generateResetPasswordAndSendToEmail(requestResetPasswordRequestBody.email)
        return ResponseApi(message = "success")
    }

    override fun resetPassword(resetPasswordRequestBody: ResetPasswordRequestBody): ResponseApi<Void> {
        authService.resetPassword(resetPasswordRequestBody.token, resetPasswordRequestBody.password, resetPasswordRequestBody.confirmPassword)
        return ResponseApi(message = "success")
    }
}