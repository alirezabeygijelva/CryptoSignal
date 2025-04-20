package dev.alireza.fms.dashboard.user

import dev.alireza.fms.common.dto.ResponseApi
import dev.alireza.fms.membership.User
import dev.alireza.fms.membership.UserService
import dev.alireza.fms.openapi.api.UserApi
import dev.alireza.fms.openapi.model.AccountInfoDTO
import dev.alireza.fms.openapi.model.ChangePasswordRequestBody
import dev.alireza.fms.openapi.model.UserAccountRequestBody
import org.springframework.beans.BeanUtils
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasRole('ROLE_USER')")
class UserController(
    private val userService: UserService
) : UserApi {

    override fun updateUserAcount(userAccountRequestBody: UserAccountRequestBody): ResponseApi<AccountInfoDTO> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as User).id
        val updatedUser = userService.updateUser(
            userId,
            userAccountRequestBody.firstName,
            userAccountRequestBody.lastName,
            userAccountRequestBody.email,
            userAccountRequestBody.phone,
            userAccountRequestBody.telegramId
        )
        val dto = AccountInfoDTO()
        BeanUtils.copyProperties(updatedUser, dto)
        dto.roles(updatedUser.roles.map { role -> role.name })
        dto.updatedAt = updatedUser.updatedAt.atOffset(ZoneOffset.UTC)
        return ResponseApi(data = dto, message = "success")
    }

    override fun changePassword(changePasswordRequestBody: ChangePasswordRequestBody): ResponseApi<Void> {
        val user = (SecurityContextHolder.getContext().authentication.principal as User)
        userService.changePassword(
            changePasswordRequestBody.password,
            changePasswordRequestBody.newPassword,
            changePasswordRequestBody.confirmNewPassword,
            user
        )
        return ResponseApi(message = "success")
    }
}