package dev.alireza.fms.notification.telegram

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot
import org.telegram.telegrambots.abilitybots.api.objects.Ability
import org.telegram.telegrambots.abilitybots.api.objects.Locality
import org.telegram.telegrambots.abilitybots.api.objects.Privacy
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient

@Component
class TelegramBot(
    @Value("\${app.telegram.bot-username}") botUsername: String,
    @Value("\${app.telegram.bot-token}") telegramBotToken: String
) : AbilityBot(OkHttpTelegramClient(telegramBotToken), botUsername) {

    fun start(): Ability {
        return Ability
            .builder()
            .name("start")
            .info("says welcome!")
            .locality(Locality.ALL)
            .privacy(Privacy.PUBLIC)
            .action {ctx -> silent.send("""
                Welcome to Financial Market Signal! 📈

                Hello! I'm FMS Bot, your personal assistant for monitoring financial markets. I'm here to help you stay updated with the latest market trends and send you notifications based on your preferences.

                Your User ID is: ${ctx.chatId()}

                Please make sure to add this User ID to your account on our website if you have already registered. Once you've linked your User ID in your account, there’s nothing more you need to do here—your notifications will be sent directly to you via this chat.

                If you need any assistance or have questions, feel free to reach out. Let's get started and keep an eye on those markets together!
            """.trimIndent(), ctx.chatId())}
            .build()
    }

    override fun creatorId(): Long {
        return 7239752950
    }
}