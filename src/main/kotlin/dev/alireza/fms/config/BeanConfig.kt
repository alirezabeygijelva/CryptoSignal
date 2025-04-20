package dev.alireza.fms.config

import dev.alireza.fms.notification.telegram.TelegramBot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication

@Configuration
class BeanConfig {

    @Autowired
    lateinit var telegramBot: TelegramBot

    @Value("\${app.telegram.bot-token}")
    lateinit var telegramBotToken: String

    @Bean
    fun telegramBotApplication() : TelegramBotsLongPollingApplication {
        // Instantiate Telegram Bots API
        val botsApplication = TelegramBotsLongPollingApplication()
        // Register your newly created AbilityBot
        botsApplication.registerBot(telegramBotToken, telegramBot)
        return botsApplication
    }
}