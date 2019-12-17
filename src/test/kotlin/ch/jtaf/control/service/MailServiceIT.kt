package ch.jtaf.control.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MailServiceIT {

    @Autowired
    lateinit var mailService: MailService

    @Test
    fun sendText() {
        mailService.sendText("noreply@jtaf.io", "simon@martinelli.ch", "Test Plain", "Test")
    }

    @Test
    fun sendHTML() {
        mailService.sendText("noreply@jtaf.io", "simon@martinelli.ch", "Test HTML", "<h1>Test</h1>")
    }
}