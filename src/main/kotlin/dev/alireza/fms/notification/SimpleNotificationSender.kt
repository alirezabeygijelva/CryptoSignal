package dev.alireza.fms.notification

import dev.alireza.fms.notification.email.EmailMessage
import dev.alireza.fms.notification.email.EmailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Service
class SimpleNotificationSender(
    private val emailService: EmailService,
    @Value("\${app.telegram.bot-token}") private val telegramBotToken: String
) : NotificationSender {

    val logger: Logger = LoggerFactory.getLogger(SimpleNotificationSender::class.java)
    private val telegramClient = OkHttpTelegramClient(telegramBotToken)

    override fun send(notification: Notification) {
        logger.info("[Simple Notification Sender] - Send notification: $notification")
        if (notification.type.equals(NotificationType.EMAIL)) {
            emailService.send(EmailMessage(notification.recipientEmail!!, notification.subject!!, notification.text))
        }
    }

    override fun sendAll(notifications: List<Notification>) {
        logger.info("[Simple Notification Sender] - Send all notifications: $notifications")
        val groupedNotifications = notifications.groupBy { it.type }
        for (type in groupedNotifications.keys) {
            if (type.equals(NotificationType.EMAIL)) {
                emailService.sendAll(groupedNotifications[type]
                    ?.map { notification ->
                        EmailMessage(
                            notification.recipientEmail!!,
                            notification.subject!!,
                            notification.text
                        )
                    }
                    ?: emptyList())
            }
            if (type.equals(NotificationType.TELEGRAM)) {
                groupedNotifications[type]?.filter { notification -> notification.telegramId != null }
                    ?.forEach {
                        val sendMessage = SendMessage(it.telegramId!!, "${it.subject} \n\n${it.text}")
                        telegramClient.executeAsync(sendMessage)
                    }
            }
        }
    }
}