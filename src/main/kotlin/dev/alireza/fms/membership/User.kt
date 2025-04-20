package dev.alireza.fms.membership

import java.time.Instant

data class User(
    var id: Long,
    var clientId: Long,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phone: String,
    val telegramId: String?,
    var password: String,
    var enabled: Boolean,
    var roles: List<Role>,
    var createdAt: Instant,
    var updatedAt: Instant
)
