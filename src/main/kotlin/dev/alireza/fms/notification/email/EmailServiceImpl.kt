package dev.alireza.fms.notification.email

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
    private val javaMailSender: JavaMailSender,
    @Value("\${spring.mail.username}") private val senderEmail: String
) : EmailService {

    private val logger: Logger = LoggerFactory.getLogger(EmailServiceImpl::class.java)

    override fun send(message: EmailMessage) {
        try {
            val msg = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(msg, true, "UTF-8")
            helper.setFrom(senderEmail)
            helper.setTo(message.to)
            helper.setSubject(message.subject)
            helper.setText(message.text, true)
            javaMailSender.send(msg)
        } catch (e: Exception) {
            logger.error("There is a problem to send an email", e)
        }
    }

    override fun sendAll(messages: List<EmailMessage>) {
        if (messages.isNotEmpty()) {
            try {
                val mimeMessages = messages.map { message ->
                    val msg = javaMailSender.createMimeMessage()
                    val helper = MimeMessageHelper(msg, true, "UTF-8")
                    helper.setFrom(senderEmail)
                    helper.setTo(message.to)
                    helper.setSubject(message.subject)
                    helper.setText(message.text, true)
                    return@map msg
                }
                javaMailSender.send(*mimeMessages.toTypedArray())
            } catch (e: Exception) {
                logger.error("There is a problem to send list of emails", e)
            }
        }
    }
}