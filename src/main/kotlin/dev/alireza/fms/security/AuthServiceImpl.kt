package dev.alireza.fms.security

import dev.alireza.fms.common.constant.ApiErrorConstant
import dev.alireza.fms.common.exception.ResponseException
import dev.alireza.fms.config.ConfigProperties
import dev.alireza.fms.membership.UserContext
import dev.alireza.fms.membership.UserRepository
import dev.alireza.fms.membership.UserService
import dev.alireza.fms.notification.email.EmailMessage
import dev.alireza.fms.notification.email.EmailService
import dev.alireza.fms.openapi.model.SignupUserDTO
import dev.alireza.fms.security.jwt.JwtUtils
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class AuthServiceImpl(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val jwtUtils: JwtUtils,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    private val properties: ConfigProperties
) : AuthService {

    override fun generateToken(username: String, password: String): AuthToken {
        val user = userService.loadUserByUsername(username) as UserContext
        if (passwordEncoder.matches(password, user.password)) {
            return AuthToken(
                accessToken = jwtUtils.generateToken(user),
                refreshToken = refreshTokenRepository.insertNewRefreshToken(
                    user.principal.id,
                    LocalDateTime.now(ZoneOffset.UTC).plusDays(30)
                ),
                expiresIn = 24 * 60 * 60,
                tokenType = "Bearer"
            )
        }
        throw ResponseException(
            HttpStatus.BAD_REQUEST,
            ApiErrorConstant.INVALID_USERNAME_OR_PASSWORD
        )
    }

    override fun renewToken(refreshToken: String, userId: Long): AuthToken {
        if (refreshTokenRepository.hasAnyUnexpiredToken(userId = userId, token = refreshToken)) {
            val user = userService.loadUserById(userId) as UserContext
            return AuthToken(
                accessToken = jwtUtils.generateToken(user),
                refreshToken = refreshToken,
                expiresIn = 24 * 60 * 60,
                tokenType = "Bearer"
            )
        }
        throw ResponseException(
            HttpStatus.BAD_REQUEST,
            ApiErrorConstant.INVALID_REFRESH_TOKEN
        )
    }

    override fun signupNewUser(signupUserDTO: SignupUserDTO) {
        if (signupUserDTO.password == signupUserDTO.confirmPassword) {
            userService.createUser(
                signupUserDTO.firstName,
                signupUserDTO.lastName,
                signupUserDTO.email,
                signupUserDTO.phone,
                passwordEncoder.encode(signupUserDTO.password)
            )
            return
        }
        throw ResponseException(
            HttpStatus.BAD_REQUEST,
            ApiErrorConstant.PASSWORD_NOT_MATCH
        )
    }

    override fun generateResetPasswordAndSendToEmail(email: String) {
        userRepository.findByEmail(email).ifPresentOrElse( { user ->
            run {
                val resetToken = UUID.randomUUID().toString()
                userRepository.insertResetPasswordToken(resetToken, user.id, LocalDateTime.now(ZoneOffset.UTC).plusMinutes(10))
                emailService.send(EmailMessage(email, "Reset Password", properties.resetPasswordUrl.format(resetToken)))
            }
        }, { throw ResponseException(
            HttpStatus.BAD_REQUEST,
            ApiErrorConstant.USER_NOT_FOUND
        )
        })
    }

    @Transactional
    override fun resetPassword(resetToken: String, password: String, confirmPassword: String) {
        if (password == confirmPassword) {
            userRepository.findUserByResetPasswordToken(resetToken).ifPresentOrElse({ user ->
                run {
                    userRepository.updatePassword(user.id, passwordEncoder.encode(password))
                    userRepository.useResetPasswordToken(resetToken)
                }
            }, { throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.INVALID_RESET_PASSWORD_TOKEN
            )
            })
            return
        }
        throw ResponseException(
            HttpStatus.BAD_REQUEST,
            ApiErrorConstant.PASSWORD_NOT_MATCH
        )
    }
}