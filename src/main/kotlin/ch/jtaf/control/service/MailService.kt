package ch.jtaf.control.service

import com.sendgrid.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class MailService(private val sendGrid: SendGrid) {

    private val logger = LoggerFactory.getLogger(MailService::class.java)

    fun sendText(from: String, to: String, subject: String, body: String) {
        sendEmail(from, to, subject, Content("text/plain", body))
    }

    fun sendHTML(from: String, to: String, subject: String, body: String) {
        sendEmail(from, to, subject, Content("text/html", body))
    }

    private fun sendEmail(from: String, to: String, subject: String, content: Content) {
        val mail = Mail(Email(from), subject, Email(to), content)
        mail.setReplyTo(Email("info@72.services"))

        val request = Request()
        request.method = Method.POST
        request.endpoint = "mail/send"
        request.body = mail.build()

        try {
            sendGrid.api(request)
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
    }

    companion object {
        const val DEFAULT_FROM = "noreply@jtaf.io"
    }
}