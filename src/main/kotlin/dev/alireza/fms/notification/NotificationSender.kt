package dev.alireza.fms.notification

interface NotificationSender {

    fun send(notification: Notification)

    fun sendAll(notifications: List<Notification>)
}