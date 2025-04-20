package dev.alireza.fms.notification

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class NotificationListener(
    private val simpleNotificationSender: SimpleNotificationSender
) {

    @Async
    @EventListener(NotificationSendAllEvent::class)
    fun sendAllEventHandler(event: NotificationSendAllEvent) {
        val mergedNotifications = mergeNotifications(event.notifications)
        simpleNotificationSender.sendAll(mergedNotifications)
    }
}

// -----------------------------
// Private Helper
// -----------------------------

private fun mergeNotifications(notifications: List<Notification>): List<Notification> {
    return notifications
        .filter { it.type != NotificationType.UNKNOWN }
        .groupBy { it.clinetId to it.type }
        .map { (key, value) ->
            val combinedText = value.joinToString(separator = "\n") { it.text }
            val firstNotification = value.first()
            firstNotification.copy(text = combinedText)
        }
}