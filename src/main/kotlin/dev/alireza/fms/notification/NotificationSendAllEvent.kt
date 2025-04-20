package dev.alireza.fms.notification

import org.springframework.context.ApplicationEvent

class NotificationSendAllEvent(
    val notifications: List<Notification>, source: Any
) : ApplicationEvent(source) {
}