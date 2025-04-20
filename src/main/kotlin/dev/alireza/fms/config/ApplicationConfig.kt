package dev.alireza.fms.config

import dev.alireza.fms.market.binance.BinanceStartEvent
import dev.alireza.fms.membership.UserRepository
import dev.alireza.fms.notification.telegram.TelegramBot
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(ConfigProperties::class)
class ApplicationConfig(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val telegramBot: TelegramBot,
    private val userRepository: UserRepository,
    private val properties: ConfigProperties,
    private val passwordEncoder: PasswordEncoder
) {

    @EventListener(ContextRefreshedEvent::class)
    fun onRefreshed() {
        applicationEventPublisher.publishEvent(BinanceStartEvent(this))

        // Duo this issue: https://github.com/rubenlagus/TelegramBots/issues/1354
        telegramBot.onRegister()

        // Initialize Roles and Admin
        initializeRolesAndAdmin()
    }

    // -----------------------------
    // Private Handler
    // -----------------------------

    private fun initializeRolesAndAdmin() {
        userRepository.insertRole("ROLE_USER", "read")
        userRepository.insertRole("ROLE_ADMIN", "read,write,delete,update")
        if (userRepository.findByIdentifier(properties.admin.username).isEmpty) {
            userRepository.insertAdmin(
                "Admin",
                "Admin",
                properties.admin.username,
                "123456789",
                passwordEncoder.encode(properties.admin.password)
            )
        }
    }
}