package dev.alireza.fms.membership

import java.time.LocalDateTime
import java.util.Optional

interface UserRepository {

    fun findById(id: Long): Optional<User>

    fun findByClientId(clientId: Long): Optional<User>

    fun findByEmail(email: String): Optional<User>

    fun findByPhone(phone: String): Optional<User>

    fun findByIdentifier(identifier: String): Optional<User>

    fun insertUser(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        encodedPassword: String
    )

    fun updateUser(
        id: Long,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        telegramId: String?
    )

    fun insertAdmin(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        encodedPassword: String
    )

    fun insertRole(name: String, authorities: String)

    fun insertResetPasswordToken(resetToken: String, userId: Long, expiresAt: LocalDateTime)

    fun isResetPasswordTokenValid(resetToken: String): Boolean

    fun findUserByResetPasswordToken(resetToken: String): Optional<User>

    fun updatePassword(userId: Long, encodedPassword: String)

    fun useResetPasswordToken(resetToken: String)
}