package dev.alireza.fms.notification

data class Notification(
    val clinetId: Long,
    val type: NotificationType,
    val recipientEmail: String? = null,
    val recipientPhoneNumber: String? = null,
    val telegramId: String? = null,
    val text: String,
    val subject: String? = null,
    val metadata: Map<String, Any>? = null
)