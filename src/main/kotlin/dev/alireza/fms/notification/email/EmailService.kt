package dev.alireza.fms.notification.email

import org.springframework.scheduling.annotation.Async

interface EmailService {

    @Async
    fun send(message: EmailMessage)

    @Async
    fun sendAll(messages: List<EmailMessage>)
}