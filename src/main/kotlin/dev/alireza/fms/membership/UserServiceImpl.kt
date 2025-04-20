package dev.alireza.fms.membership

import dev.alireza.fms.common.constant.ApiErrorConstant
import dev.alireza.fms.common.exception.ResponseException
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Primary
@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    @Lazy private val passwordEncoder: PasswordEncoder
) : UserService {
    override fun loadUserById(userId: Long): UserDetails {
        val user = userRepository.findById(userId).orElseThrow {
            throw ResponseException(
                HttpStatus.NOT_FOUND,
                ApiErrorConstant.USER_NOT_FOUND
            )
        }
        return UserContext(user)
    }

    override fun createUser(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        encodedPassword: String
    ) {
        verifyUserInsertion(email, phone)
        userRepository.insertUser(
            firstName,
            lastName,
            email,
            phone,
            encodedPassword
        )
    }

    override fun updateUser(
        id: Long,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        telegramId: String?
    ): User {
        verifyUserUpdate(id, email, phone)
        userRepository.updateUser(id, firstName, lastName, email, phone, telegramId)
        return userRepository.findById(id).get()
    }

    override fun changePassword(oldPassword: String, newPassword: String, confirmNewPassword: String, user: User) {
        if (newPassword == confirmNewPassword) {
            if (passwordEncoder.matches(oldPassword, user.password)) {
                userRepository.updatePassword(user.id, passwordEncoder.encode(newPassword))
                return
            }
            throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.INVALID_PASSWORD
            )
        }
        throw ResponseException(
            HttpStatus.BAD_REQUEST,
            ApiErrorConstant.PASSWORD_NOT_MATCH
        )
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        // username is an abstraction of each unique identifier of the user.
        // Like email, phone number or indeed a username.
        if (username == null) throw ResponseException(
            HttpStatus.UNAUTHORIZED,
            ApiErrorConstant.INVALID_USERNAME
        )
        val user = userRepository.findByIdentifier(username).orElseThrow {
            throw ResponseException(
                HttpStatus.NOT_FOUND,
                ApiErrorConstant.USER_NOT_FOUND
            )
        }
        return UserContext(user)
    }

    // -------------------------
    // Private Helper
    // -------------------------

    private fun verifyUserInsertion(email: String, phone: String) {
        if (userRepository.findByEmail(email).isPresent) {
            throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.UNAVAILABLE_EMAIL
            )
        }
        if (userRepository.findByPhone(phone).isPresent) {
            throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.UNAVAILABLE_PHONE
            )
        }
    }

    private fun verifyUserUpdate(userId: Long, email: String, phone: String) {
        val findUserByEmail = userRepository.findByEmail(email)
        if (findUserByEmail.isPresent && findUserByEmail.get().id != userId) {
            throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.UNAVAILABLE_EMAIL
            )
        }
        val findUserByPhone = userRepository.findByPhone(phone)
        if (findUserByPhone.isPresent && findUserByPhone.get().id != userId) {
            throw ResponseException(
                HttpStatus.BAD_REQUEST,
                ApiErrorConstant.UNAVAILABLE_PHONE
            )
        }
    }
}