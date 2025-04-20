package dev.alireza.fms.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class ConfigProperties(
    val security: Security,
    val market: Market,
    val telegram: Telegram,
    val admin: Admin,
    val allowOrigins: Array<String>,
    val allowOriginPatterns: Array<String>,
    val resetPasswordUrl: String
) {
    data class Market(val general: General) {
        data class General(val cacheTtl: Long)
    }

    data class Security(val jwtSecretKey: String)

    data class Telegram(val botToken: String, val botUsername: String)

    data class Admin(val username: String, val password: String)
}